package com.carlkuesters.fifachampions.menu;

import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PlayerDetailsContainer {
    private Container container;
    private Label[][] lblSkills;
}
