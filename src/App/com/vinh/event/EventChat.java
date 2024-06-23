package App.com.vinh.event;

import App.com.vinh.model.Model_Receive_Message;
import App.com.vinh.model.Model_Send_Message;

public interface EventChat {

    public void sendMessage(Model_Send_Message data);

    public void receiveMessage(Model_Receive_Message data);
}
