/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.stephen.blogsite.dtos;

import java.util.Objects;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 *
 * @author stephenespinal
 *
 */

public class HashTag {

    int hashTagId;
    String hashTagName;

    public int getHashTagId() {
        return hashTagId;
    }

    public void setHashTagId(int hashTagId) {
        this.hashTagId = hashTagId;
    }

    public String getHashTagName() {
        return hashTagName;
    }

    public void setHashTagName(String hashTagName) {
        this.hashTagName = hashTagName;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + this.hashTagId;
        hash = 11 * hash + Objects.hashCode(this.hashTagName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HashTag other = (HashTag) obj;
        if (this.hashTagId != other.hashTagId) {
            return false;
        }
        if (!Objects.equals(this.hashTagName, other.hashTagName)) {
            return false;
        }
        return true;
    }

}
