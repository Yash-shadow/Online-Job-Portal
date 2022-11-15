package com.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SkillDTO {

    private String skillName;

    public SkillDTO(String skillName) {
        super();
        this.skillName = skillName;
    }
    
}
