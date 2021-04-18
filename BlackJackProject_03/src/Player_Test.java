import com.heart.service.PlayerService;

public class Player_Test {

	public static void main(String[] args) {
		
		PlayerService player = new PlayerService();
		player.inputGamer();
		player.inputIntiMoney();
		player.betMoney();
	}
}
