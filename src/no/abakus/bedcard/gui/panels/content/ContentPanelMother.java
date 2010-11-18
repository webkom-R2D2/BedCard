package no.abakus.bedcard.gui.panels.content;

import java.awt.Color;
import java.awt.FlowLayout;

import no.abakus.bedcard.gui.BedCard;
import no.abakus.bedcard.gui.panels.BedCardPanel;
import no.abakus.bedcard.logic.Logic;

public abstract class ContentPanelMother extends BedCardPanel {
	protected Logic logic;
	protected ContentPanel contentPanel;
	public ContentPanelMother(BedCard bedcard, Logic logic, ContentPanel contentPanel) {
		super(bedcard);
		this.logic = logic;
		this.contentPanel = contentPanel;
		width = bedcard.getDrawWidth();
		height = bedcard.getDrawHeight()-BedCard.BottomStatusPanelHeight-BedCard.MenuPanelHeight;
		setPanelSizes();
		setLayout(new FlowLayout(FlowLayout.CENTER, 0,0));
		setBackground(Color.white);
	}
	public abstract void refreshData();
	public abstract void entered();
	public abstract void fading(boolean state);
}
