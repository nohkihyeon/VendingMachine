/**
 * 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * 상태 패턴
 * 자동판매기 판매 제품: 판매하고 있는 상품의 종류를 나타내는 열거형
 */
public class VendingMachine {
	// 자판기 보유 재고
	protected InventoryStock inventoryStock = new InventoryStock();
	// 자판기 보유 현금
	protected CashRegister cashRegister = new CashRegister();
	// 고객이 투입한 현금
	protected CashRegister userCashRegister = new CashRegister();
	
	private VendingState currentState;
	public VendingMachine() {
		currentState = VendingState.NOCOINSTATE;
	}
	public void changeState(VendingState nextState) {
		currentState = nextState;
	}
	
	// InventoryStock 상호작용
	public boolean isEmpty() {
		return inventoryStock.isEmpty();
	}
	public int getNumberOfItems(Item item) {
		return inventoryStock.getNumberOfItems(item);
	}
	public void clearItems() {
		inventoryStock.clear();
	}
	public void supplementItems(Item item, int amount) {
		inventoryStock.addItem(item, amount);
	}
	public void removeItem(Item item) {
		inventoryStock.removeItem(item);
	}
	// 현재 재고가 있는 제품 중 가장 저렴한 제품의 가격
	public int minPrice() {
		return inventoryStock.minPrice();
	}
	// cashRegister 상호작용
	public int getBalance() {
		return cashRegister.getBalance();
	}
	public void setCash(Currency currency, int amount) {
		cashRegister.set(currency, amount);
	}
	public int getAmount(Currency currency) {
		return cashRegister.getAmount(currency);
	}
	
	// userCashRegister 상호작용
	public int getInsertedBalance() {
		return userCashRegister.getBalance();
	}
	public void setUserCashRegister(CashRegister changeRegister) {
		userCashRegister = changeRegister;
	}
	// 실제 투입된 돈을 처리하는 메소드
	// 고객이 투입한 돈은 자판기 보유 돈에도 포함하여 처리함
	public void addCash(Currency currency, int amount) {
		System.out.println("asdfasdfasdf"+currency+" "+amount);
		userCashRegister.add(currency, amount);
		cashRegister.add(currency, amount);
	}
	
	// vendingMachine 자체와 상호작용
	public void insertCash(Currency currency, int amount) {
		// 완성하시오.
		currentState.insertCash(this, currency, amount);
	}
	public void selectItem(Item item) throws ChangeNotAvailableException {
		// 완성하시오.
		currentState.selectItem(this, item);
	}
	
	public void cancel() {
		currentState.cancel(this);
	}
	
	public void debugPrint() {
		System.out.println("=========================");
		cashRegister.debugPrint();
		inventoryStock.debugPrint();
	}
}
