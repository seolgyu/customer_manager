package cm.cmm;

public class CustomerDetailDTO {
	private String id;
	private String pwd;
    private String name;
    private String tel;
    private String email;
    private String address;
    private String reg;
    private String rrn;
    private String dormancy;
    private String class_Id;
    private String class_Level;
    private String remain_Mil;
    private String total_cost;
    
	public String getTotal_cost() {
		return total_cost;
	}
	public void setTotal_cost(String total_cost) {
		this.total_cost = total_cost;
	}
	public String getRemain_Mil() {
		return remain_Mil;
	}
	public void setRemain_Mil(String remain_Mil) {
		this.remain_Mil = remain_Mil;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getReg() {
		return reg;
	}
	public void setReg(String reg) {
		this.reg = reg;
	}
	public String getRrn() {
		return rrn;
	}
	public void setRrn(String rrn) {
		this.rrn = rrn;
	}
	public String getDormancy() {
		return dormancy;
	}
	public void setDormancy(String dormancy) {
		this.dormancy = dormancy;
	}
	public String getClass_Id() {
		return class_Id;
	}
	public void setClass_Id(String class_Id) {
		this.class_Id = class_Id;
	}
	public String getClass_Level() {
		return class_Level;
	}
	public void setClass_Level(String class_Level) {
		this.class_Level = class_Level;
	}

}
