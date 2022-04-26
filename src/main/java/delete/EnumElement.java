package delete;

import net.labymod.gui.elements.DropDownMenu;
import net.labymod.main.LabyMod;
import net.labymod.main.lang.LanguageManager;
import net.labymod.settings.elements.DropDownElement;
import net.labymod.utils.Consumer;

public class EnumElement {

	public EnumElement(String name){
		final DropDownMenu<meinEnum> alignmentDropDownMenu = new DropDownMenu<meinEnum>( name /* Display name */, 0, 0, 0, 0 )
                .fill( meinEnum.values() );
DropDownElement<meinEnum> alignmentDropDown = new DropDownElement<meinEnum>( "Custom Alignment", alignmentDropDownMenu );

// Set selected entry
alignmentDropDownMenu.setSelected( meinEnum.A );

// Listen on changes
alignmentDropDown.setChangeListener( new Consumer<meinEnum>() {
    @Override
    public void accept( meinEnum alignment ) {
        System.out.println( "New selected alignment: " + alignment.name() );
    }
} );

// Change entry design (optional)
alignmentDropDownMenu.setEntryDrawer( new DropDownMenu.DropDownEntryDrawer() {
    @Override
    public void draw( Object object, int x, int y, String trimmedEntry ) {
        // We translate the value and draw it
        String entry = object.toString().toLowerCase();
        LabyMod.getInstance().getDrawUtils().drawString( LanguageManager.translate( entry ), x, y );
    }
} );
// Add to sublist
//subSettings.add( alignmentDropDown );
		
	}
	
	public enum meinEnum {
		A(),
		B(),
		C();

		
		
	}
	
}
