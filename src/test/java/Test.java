import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Map;

/**
 * @ClassName Test
 * @Description TODO
 * @Author LYR
 * @Date 2019/3/27 15:20
 * @Version 1.0
 **/
public class Test {

    public static void main(String[] args) {

        //进行加密
//        BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
//        System.out.println(encoder.encode("admin"));

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String sql = "SELECT * FROM sys_resource c LEFT JOIN ( SELECT b.res_id FROM sys_role_res b LEFT JOIN ( SELECT role_id FROM sys_user_role WHERE user_id = '2' ) a ON a.role_id = b.role_id ) d ON c.id = d.res_id";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        System.out.println();

    }
}
