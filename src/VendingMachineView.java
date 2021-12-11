import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * 상태 패턴
 * 자동 판매기 뷰
 */
public class VendingMachineView extends Application {
	private Button[] itemButtons = new Button[6];
	private Button insertCoinButton = new Button("돈 투입");
	private Button cancelButton = new Button("취소");
	private TextField currentUserBalanceField = new TextField();
	private VendingMachine vendingMachine = new VendingMachine();
	
	// 뷰의 메뉴 생성 
	private MenuBar createMenuBar() {
		MenuItem stockSetupItem = new MenuItem("재고 정리");
		MenuItem balanceSetupItem = new MenuItem("돈 정리");
		stockSetupItem.setOnAction(e->{
			VendingMachineUtility.showStockSetupDialog(vendingMachine);
			enableDisableButtons();
		});
		balanceSetupItem.setOnAction(e->
			VendingMachineUtility.showBalanceSetupDialog(vendingMachine));	
		Menu setupMenu = new Menu("자판기 설정");
		setupMenu.getItems().addAll(stockSetupItem, balanceSetupItem);
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().add(setupMenu);
		return menuBar;
	}
	
	// 제품 선택 뷰 생성
	private GridPane createItemsPane() {
		GridPane centerPane = new GridPane();
		centerPane.setAlignment(Pos.CENTER);
		Item[] items = Item.values();
		TextField[] prices = new TextField[items.length];
		for(int i=0; i<prices.length; i++) {
			Item currentItem = items[i];
			prices[i] = new TextField();
			itemButtons[i] = new Button();
			itemButtons[i].setOnAction(e->processItemSelect(currentItem));
			prices[i].setMaxWidth(120d);
			prices[i].setEditable(false);
			prices[i].setText(String.format("%,d원", currentItem.price));
			prices[i].setAlignment(Pos.CENTER);
			centerPane.add(prices[i], i%3, (i/3==0)?1:3);
			centerPane.add(itemButtons[i], i%3, (i/3==0)?0:2);
		}
		itemButtons[0].setGraphic(new ImageView(new Image("cider.jpg")));
		itemButtons[1].setGraphic(new ImageView(new Image("cola.jpeg")));
		itemButtons[2].setGraphic(new ImageView(new Image("pepsi.jpg")));
		itemButtons[3].setGraphic(new ImageView(new Image("pocari.jpg")));
		itemButtons[4].setGraphic(new ImageView(new Image("top.jpg")));
		itemButtons[5].setGraphic(new ImageView(new Image("max.jpg")));
		enableDisableButtons();
		return centerPane;
	}
	
	private void processItemSelect(Item item) {
		try {
			vendingMachine.selectItem(item);
			currentUserBalanceField.setText(
				String.format("%,d원", vendingMachine.getInsertedBalance()));
			enableDisableButtons();
			insertCoinButton.requestFocus();
		} catch (ChangeNotAvailableException exception) {
			VendingMachineUtility.showInfoDialog("한기대 자판기 APP", 
				"거스름을 드릴 수 없어 취소됩니다. 다른 종류의 동전/지폐를 이용해 주세요.");
			currentUserBalanceField.setText("0원");
		}
	}
	
	// 상품 구매 가능 여부 활성화
	private void enableDisableButtons(){
		Item[] items = Item.values();
		int currentInsertedBalance = vendingMachine.getInsertedBalance();
		for(int i=0; i<items.length; i++) {
			itemButtons[i].setDisable(
				(items[i].price>currentInsertedBalance)||
					vendingMachine.getNumberOfItems(items[i])==0);
		}
	}
	
	private HBox createButtonPane() {
		HBox buttonPane = new HBox();
		buttonPane.setAlignment(Pos.CENTER);
		buttonPane.setPadding(new Insets(10d));
		buttonPane.setSpacing(10d);
		currentUserBalanceField.setEditable(false);
		currentUserBalanceField.setText(String.format("%,d원", vendingMachine.getBalance()));
		currentUserBalanceField.setAlignment(Pos.BASELINE_RIGHT);
		
		insertCoinButton.setOnAction(e->processInsertCash());
		cancelButton.setOnAction(e->{
			vendingMachine.cancel();
			currentUserBalanceField.setText(
				String.format("%,d원", vendingMachine.getInsertedBalance()));			
		});
		
		buttonPane.getChildren().addAll(
			currentUserBalanceField, insertCoinButton, cancelButton);
		return buttonPane;
	}

	private void processInsertCash() {
		VendingMachineUtility.showInsertCoinDialog().ifPresent(cash->
			vendingMachine.insertCash(cash.getCurrency(), cash.getAmount()));
		enableDisableButtons();
		currentUserBalanceField.setText(
			String.format("%,d원", vendingMachine.getInsertedBalance()));
	}
	
	@Override
	public void start(Stage mainStage) throws Exception {
		BorderPane mainPane = new BorderPane();
		mainPane.setTop(createMenuBar());
		mainPane.setCenter(createItemsPane());
		mainPane.setBottom(createButtonPane());
		mainStage.setTitle("KoreaTech Vending Machine App");
		mainStage.setScene(new Scene(mainPane));
		mainStage.show();
		insertCoinButton.requestFocus();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
