package sk.solidsoft.pripomen;

import java.awt.MenuItem;

/**
 * T�to trieda sl��i len na to, aby v kon�truktore po�adovala
 * ako parameter exempl�r triedy PripomenUI, z ktor�ho
 * sa vytvoril exepl�r triedy MenuItemMoje (tejto triedy).
 * V�aka tomu sa viem dosta� k exepl�ru triedy PripomenUI
 * z exempl�ru triedy MenuItemMoje (tejto triedy).
 * 
 * @author Mari�n D�ne�
 */

final class MenuItemMoje extends MenuItem{
	private static final long serialVersionUID = 1L;
	final PripomenUI pripomenUI;

	MenuItemMoje(PripomenUI pripomenUI) {
		super();
		this.pripomenUI = pripomenUI;
	}
}
