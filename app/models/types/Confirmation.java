package models.types;

public enum Confirmation {
	TENTATIVE, CONFIRMED, SIGNED, DEREGISTERED;
	
	//TODO: i18n
	
	@Override
	public String toString() {

		if (super.toString().equals("SIGNED")) {
			return "Nimmt Teil";
		}
		if (super.toString().equals("TENTATIVE")) {
			return "Vorläufig";
		}
		if (super.toString().equals("CONFIRMED")) {
			return "Bestätigt";
		}
		if (super.toString().equals("DEREGISTERED")) {
			return "Abgemeldet";
		}
		return super.toString();
	};
	
}
