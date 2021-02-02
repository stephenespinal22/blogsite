/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.stephen.blogsite.services;

import org.springframework.dao.DuplicateKeyException;

/**
 *
 * @author stephenespinal
 */
public class DuplicateIdException extends DuplicateKeyException  {
    
        public DuplicateIdException(String message) {
        super(message);
    }

    public DuplicateIdException(String message,
            Throwable cause) {
        super(message, cause);
    }

}