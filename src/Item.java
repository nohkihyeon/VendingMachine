
/**
 * 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * 상태 패턴
 * 자동판매기 판매 제품: 판매하고 있는 상품의 종류를 나타내는 열거형
 */
public enum Item {
	Cider(800), Cola(800), Pepsi(800), 
	Pocari(1000), Top(1200), Max(800);
	private static String[] itemNames = {
		"사이다", "콜라", "펩시", "포카리", "티오피", "맥스"
	};
	public final int price;
	private Item(int price) {
		this.price = price;
	}
	@Override public String toString() {
		return itemNames[this.ordinal()];
	}
}
