package cm.man;
// 인터페이스
import java.sql.SQLException;
import java.util.List;

public interface AdminDAO {
    public int insertAdmin(AdminDTO dto) throws SQLException;      
    public List<AdminDTO> listAdmin();
    
}
