package com.jorgedelarosa.aimiddleware.application.port.in;

/**
 * @author jorge
 */
public interface UserInteractUseCase {

    public void execute(Command cmd);
    
    public record Command(String text){
    }
}
