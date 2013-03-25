/*
 * 11.11.2007 
 *
 * Udo Simon
 * 
 * Beschreibung:
 * 
 * Interface für das Panel APanelTonArt
 */
package AChord.AClasses;

import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;

abstract public interface APanelTonArtInterface
{
	abstract public void onRadioButtonTon(final String ton, ItemEvent evt);
	public void onKeyEvent(KeyEvent e);
	abstract public void onRadioButtonHalbTon(final String halbTon, ItemEvent evt);
	abstract public void onRadioButtonTonGeschlecht(final String geschlecht, ItemEvent evt);
	abstract public void onRadioButtonTonKlang(final String klang, ItemEvent evt);
}
