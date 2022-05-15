package com.rashm1n.springwebsocketapi.controllers;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    /**
     * return value of the method is serialized as a payload and sent
     * not to the "brokerChannel" but to the
     * "clientOutboundChannel", effectively replying directly
     * to the client rather than broadcasting through the broker
     **/
    @SubscribeMapping("/subscribe")
    public String sendSubscriptionResponse() {
        return "subscribed";
    }

    /**
     * SEND Messages which comes to /app/request1/ path
     */
    @MessageMapping("/request1")
    public void handleRequestOne(String message) {
        System.out.println("Message Received: "+message);
    }

    /**
     * SEND Messages which comes to /app/request2/ path
     * Message is then forwarded to /topic/requests via the broker channel to be broadcasted with MESSAGE frame
     */
    @MessageMapping("/request2")
    @SendTo("/topic/requests")
    public String receiveReqAndForward(String message) {
        return "Received message: "+message+", forwarded to Queue";
    }

    @MessageMapping("/request3")
    @SendTo("/topic/requests")
    public String receiveAndGenerateException(String message) {
        if (message.contains("exception")) {
            throw  new RuntimeException("Exception Occurred");
        } else {
            return "Without Exception";
        }
    }

    @MessageExceptionHandler // used to handle exceptions in the @SubscribeMapping and @MessageMapping annotated controllers
    @SendTo("/queue/errors")
    public String handleExceptions(Throwable exception) {
        return "Server exception: " + exception.getMessage();
    }
}
