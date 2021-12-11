/**
 * 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * 상태 패턴
 * 자동판매기 거스름 제공 불가 예외: checked 예외 
 */
public class ChangeNotAvailableException extends Exception {
	private static final long serialVersionUID = -6480831563798786756L;
	public ChangeNotAvailableException() {
		super("거스름돈 준비 불가");
	}
	public ChangeNotAvailableException(String msg) {
		super(msg);
	}
}
