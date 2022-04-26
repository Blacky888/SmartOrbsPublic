package delete;

import net.labymod.settings.elements.SettingsElement;

public class PlaceholderElement extends SettingsElement {

	private SettingsElement parent;
	
	public PlaceholderElement(SettingsElement parent) {
		super("", "");
		this.parent = parent;
	}

	public SettingsElement getParent() {
		return parent;
	}
	
	public void drawDescription(int a, int b, int c) {}
	public int getEntryHeight() { return 0; }
	public void keyTyped(char a, int b) {}
	public void mouseClickMove(int a, int b, int c) {}
	public void mouseClicked(int a, int b, int c) {}
	public void mouseRelease(int a, int b, int c) {}
	public void unfocus(int a, int b, int c) {}
	
	public static class IndexerElement extends SettingsElement {

		private Object key;
		
		public IndexerElement(Object key) {
			super("", "");
			this.key = key;
		}
		public void drawDescription(int a, int b, int c) {}
		public int getEntryHeight() { return 0; }
		public void keyTyped(char a, int b) {}
		public void mouseClickMove(int a, int b, int c) {}
		public void mouseClicked(int a, int b, int c) {}
		public void mouseRelease(int a, int b, int c) {}
		public void unfocus(int a, int b, int c) {}
		
		@Override
		public String toString() {
			return "IndexerElement(\"" + key + "\")";
		}

		public Object getKey() {
			return key;
		}
		
		public static boolean is(SettingsElement element, String key) {
			return element instanceof IndexerElement && ((IndexerElement)element).getKey().equals(key);
		}
		
		public boolean is(String key) {
			return getKey().equals(key);
		}
	}
}
