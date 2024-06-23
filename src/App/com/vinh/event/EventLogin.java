package App.com.vinh.event;

import App.com.vinh.model.Model_Login;
import App.com.vinh.model.Model_Register;

public interface EventLogin {

    public void login(Model_Login data);

    public void register(Model_Register data, EventMessage message);

    public void goRegister();

    public void goLogin();
}
