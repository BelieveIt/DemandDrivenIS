package dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import model.CommercialEnvironment;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import utils.DaoUtil;

public class CommercialEnvironmentDao implements Serializable{
private static final long serialVersionUID = -5644881105829827803L;
private JdbcTemplate jdbcTemplate;
public CommercialEnvironmentDao(){
	jdbcTemplate = new JdbcTemplate(DaoUtil.getDataSource());
}

public List<CommercialEnvironment> queryCommercialEnvironments(){
	String sql = "select * from COMMERCIAL_ENVIRONMENT";
	return jdbcTemplate.query(sql, new CommercialEnvironmentMapper());
}


private static final class CommercialEnvironmentMapper implements RowMapper<CommercialEnvironment> {
    public CommercialEnvironment mapRow(ResultSet rs, int rowNum) throws SQLException {
    	CommercialEnvironment commercialEnvironment = new CommercialEnvironment();
    	commercialEnvironment.setEnvironmentId(rs.getString("ENVIRONMENT_ID"));
    	commercialEnvironment.setName(rs.getString("NAME"));
        return commercialEnvironment;
    }
}
}
