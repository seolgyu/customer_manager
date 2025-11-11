package cm.Login;



import java.sql.SQLException;
import java.util.List;


public interface LoginDAO {
	public void insertMember(LoginDTO dto) throws SQLException;
	public void updateMember(LoginDTO dto) throws SQLException;
	public void deleteMember(String cus_id) throws SQLException;
	public void insertLoginHistory(String cus_id) throws Exception;
	
	public LoginDTO findById(String cus_id);
	public List<LoginDTO> listMember();
	public List<LoginDTO> listMember(String name);
	LoginDTO adminfindById(String cus_id);	
}
