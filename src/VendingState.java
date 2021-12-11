import java.util.Map.Entry;

/**
 * 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * 상태 열거형
 * 자동판매기 판매 제품: 판매하고 있는 상품의 종류를 나타내는 열거형
 *  * State Driven Transition (상태 기반 전이)
 * 열거형으로 상태 객체들을 정의. 한 자바 파일에 모든 상태 구현.
 * 상태 객체 간의 coupling tight해지는 단점이 있을 수 있다 
 */
public enum VendingState {
	SOLDSTATE{
		@Override
		public void insertCash(VendingMachine vMachine, Currency currency, int amount) {
			vMachine.addCash(currency, amount);
		}
		@Override
		public void selectItem(VendingMachine vMachine, Item item)throws ChangeNotAvailableException {
			if (vMachine.inventoryStock.getNumberOfItems(item) == 0) {
				vMachine.changeState(SOLDSTATE);
			}
			else if (!vMachine.isEmpty()) {
				tempBalance = vMachine.cashRegister.getChange(vMachine.getInsertedBalance()-item.price); 
				System.out.println(tempBalance.getBalance());
				// 자판기에 잔돈이 없는경우
				if (tempBalance.getBalance() == 0) {
					vMachine.cancel();
					vMachine.changeState(NOCOINSTATE);
					throw new ChangeNotAvailableException(); 
				}
				else {
					vMachine.userCashRegister = tempBalance;
					vMachine.inventoryStock.removeItem(item);
					// 사용자가 돈이 부족한경우
					if (vMachine.userCashRegister.getBalance() < vMachine.minPrice()) {
						vMachine.changeState(NOCOINSTATE);
						for (Entry<Currency, Integer> et : tempBalance.getEntrySet()) {
							if (et.getValue() > 0)
								vMachine.cashRegister.remove(et.getKey(), et.getValue());
						}
					}
				}
			}
			// 자판기에 매물이 없는경우
			else if(vMachine.isEmpty()) {
				vMachine.changeState(SOLDOUTSTATE);
			}
			
		}
		@Override
		public void cancel(VendingMachine vMachine) {
			System.out.println("SOLDSTATE / cancel");
			for (Entry<Currency, Integer> et : vMachine.userCashRegister.getEntrySet()) {
				if (et.getValue() > 0) {
					vMachine.cashRegister.remove(et.getKey(), et.getValue());
				}
				if (et.getValue() > 0) {
					vMachine.userCashRegister.remove(et.getKey(), et.getValue());
				}
			}
			vMachine.changeState(NOCOINSTATE);
		}
		@Override
		public void clearItem(VendingMachine vMachine) {
			vMachine.clearItems();
			vMachine.changeState(SOLDOUTSTATE);
		}
		
	},
	NOCOINSTATE{
		@Override
		public void insertCash(VendingMachine vMachine, Currency currency, int amount) {
			System.out.println("NOCOINSTATE / insertCash");
			vMachine.addCash(currency, amount);
			vMachine.changeState(SOLDSTATE);
		}
		@Override
		public void selectItem(VendingMachine vMachine, Item item) throws ChangeNotAvailableException {
			System.out.println("NOCOINSTATE / selectItem");
		}
		@Override
		public void cancel(VendingMachine vMachine) {
			System.out.println("NOCOINSTATE / cancel");
			for (Entry<Currency, Integer> et : vMachine.userCashRegister.getEntrySet()) {
				if (et.getValue() > 0)
					vMachine.cashRegister.remove(et.getKey(), et.getValue());
				if (et.getValue() > 0) {
					vMachine.userCashRegister.remove(et.getKey(), et.getValue());
				}
			}
		}
		@Override
		public void clearItem(VendingMachine vMachine) {
			vMachine.clearItems();
			vMachine.changeState(SOLDOUTSTATE);
		}
		
	},
	SOLDOUTSTATE{
		@Override
		public void insertCash(VendingMachine vMachine, Currency currency, int amount) {
			System.out.println("SOLDOUTSTATE / insertCash");
			vMachine.addCash(currency, amount);
			vMachine.changeState(SOLDSTATE);
		}
		@Override
		public void selectItem(VendingMachine vMachine, Item item) {
			System.out.println("SOLDOUTSTATE / selectItem");
		}
		@Override
		public void cancel(VendingMachine vMachine) {
			System.out.println("SOLDOUTSTATE / cancel");
			for (Entry<Currency, Integer> et : vMachine.userCashRegister.getEntrySet()) {
				if (et.getValue() > 0)
					vMachine.cashRegister.remove(et.getKey(), et.getValue());
				if (et.getValue() > 0) {
					vMachine.userCashRegister.remove(et.getKey(), et.getValue());
				}
			}
			
			vMachine.changeState(NOCOINSTATE);
		}
		@Override
		public void clearItem(VendingMachine vMachine) {
			vMachine.clearItems();
			vMachine.changeState(SOLDOUTSTATE);
		}
	};
	public void insertCash(VendingMachine vMachine, Currency currency, int amount) {}
	public void selectItem(VendingMachine vMachine, Item item) throws ChangeNotAvailableException {}
	public void cancel(VendingMachine vMachine) {}
	public void clearItem(VendingMachine vMachine) {}
	// 먼저 돈을 넣어보고 잔돈이 가능하다면 동작을 진행하기 위해서 임시 저장변수
	public CashRegister tempBalance;
}
