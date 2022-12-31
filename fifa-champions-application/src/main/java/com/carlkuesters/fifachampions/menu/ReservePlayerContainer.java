package com.carlkuesters.fifachampions.menu;

import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.component.IconComponent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReservePlayerContainer {
    private Container container;
    private Label lblPosition;
    private Label lblSkill;
    private Label lblName;
    private IconComponent playerIcon;
    private MenuElement menuElement;
}
