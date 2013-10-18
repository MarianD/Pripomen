package sk.solidsoft.pripomen;

import java.awt.MenuItem;

/**
 * T·to trieda sl˙ûi len na to, aby v konötruktore poûadovala
 * ako parameter exempl·r triedy PripomenUI, z ktorÈho
 * sa vytvoril exepl·r triedy MenuItemMoje (tejto triedy).
 * VÔaka tomu sa viem dostaù k exepl·ru triedy PripomenUI
 * z exempl·ru triedy MenuItemMoje (tejto triedy).
 * 
 * @author Mari·n DÈneö
 */

final class MenuItemMoje extends MenuItem{
	private static final long serialVersionUID = 1L;
	final PripomenUI pripomenUI;

	MenuItemMoje(PripomenUI pripomenUI) {
		super();
		this.pripomenUI = pripomenUI;
	}
}
