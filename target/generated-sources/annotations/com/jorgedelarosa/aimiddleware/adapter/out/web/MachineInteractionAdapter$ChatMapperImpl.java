package com.jorgedelarosa.aimiddleware.adapter.out.web;

import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.GenericChatMessage;
import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.GenericChatRequest;
import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.OllamaChatMessage;
import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.OllamaChatRequest;
import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.OpenRouterChatCompletionMessage;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-17T18:25:57+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.3 (Arch Linux)"
)
public class MachineInteractionAdapter$ChatMapperImpl implements MachineInteractionAdapter.ChatMapper {

    @Override
    public OllamaChatMessage toOllamaChatMessage(GenericChatMessage a) {
        if ( a == null ) {
            return null;
        }

        String thinking = null;
        String role = null;
        String content = null;

        thinking = a.reasoning();
        role = a.role();
        content = a.content();

        OllamaChatMessage ollamaChatMessage = new OllamaChatMessage( role, content, thinking );

        return ollamaChatMessage;
    }

    @Override
    public OllamaChatRequest toOllamaChatMessage(GenericChatRequest a) {
        if ( a == null ) {
            return null;
        }

        boolean think = false;
        String model = null;
        List<OllamaChatMessage> messages = null;

        think = a.reasoning();
        model = a.model();
        messages = genericChatMessageListToOllamaChatMessageList( a.messages() );

        boolean stream = false;

        OllamaChatRequest ollamaChatRequest = new OllamaChatRequest( model, messages, stream, think );

        return ollamaChatRequest;
    }

    @Override
    public OpenRouterChatCompletionMessage toOpenRouterChatCompletionMessage(GenericChatMessage a) {
        if ( a == null ) {
            return null;
        }

        String role = null;
        String content = null;
        String reasoning = null;

        role = a.role();
        content = a.content();
        reasoning = a.reasoning();

        String refusal = null;

        OpenRouterChatCompletionMessage openRouterChatCompletionMessage = new OpenRouterChatCompletionMessage( role, content, reasoning, refusal );

        return openRouterChatCompletionMessage;
    }

    protected List<OllamaChatMessage> genericChatMessageListToOllamaChatMessageList(List<GenericChatMessage> list) {
        if ( list == null ) {
            return null;
        }

        List<OllamaChatMessage> list1 = new ArrayList<OllamaChatMessage>( list.size() );
        for ( GenericChatMessage genericChatMessage : list ) {
            list1.add( toOllamaChatMessage( genericChatMessage ) );
        }

        return list1;
    }
}
