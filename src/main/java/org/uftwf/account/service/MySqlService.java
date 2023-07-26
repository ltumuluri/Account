package org.uftwf.account.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uft.plunkit.BlowfishEncryption;
import org.uft.plunkit.Member;
import org.uftwf.account.model.*;
import org.uftwf.account.util.MySqlConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by xyang on 3/2/17.
 */
public class MySqlService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MySqlService.class);
    private static MySqlService service;

    private MySqlService() {
    }

    ;

    public static MySqlService getInstance() {
        LOGGER.info("getInstance(): return MySqlService instance" + "\r\n");
        if (service == null) {
            return new MySqlService();
        } else {
            return service;
        }
    }

    public ArrayList<String> getForbiddenDomain() {
        LOGGER.info("getForbiddenDomain(): return forbiddendomain from mysql database" + "\r\n");
        Connection connection = null;
        ArrayList<String> output = new ArrayList<String>();
        if (MySqlConnectionFactory.canConnect()) {
            try {
                connection = MySqlConnectionFactory.getConnection();
                String query = "select description from forbidden_domain";
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet result = statement.executeQuery();
                while (result.next()) {
                    String domain = result.getString("description").trim();
                    output.add(domain);
                }
            } catch (Exception e) {
                // e.printStackTrace();

            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                        return output;
                    } catch (SQLException e) {

                    }
                }
            }
        }
        return output;
    }

    public ObjectDB optInNumber(String memberId) {
        Connection connection = null;
        ObjectDB output = new ObjectDB();
        if (MySqlConnectionFactory.canConnect()) {
            try {
                output.setDbStatus(true);
                connection = MySqlConnectionFactory.getConnection();
                String query = "select opt_in from member_ext where member_id=?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, memberId);
                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    output.setDbObject(result.getString("opt_in"));
                }
                return output;
            } catch (Exception e) {
                e.printStackTrace();
                return output;
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                        return output;
                    } catch (SQLException e) {

                    }
                }
            }
        } else {
            output.setDbStatus(false);
        }
        return output;
    }

    public ObjectDB uftId(String memberId) {
        Connection connection = null;
        ObjectDB output = new ObjectDB();
        if (MySqlConnectionFactory.canConnect()) {
            try {
                output.setDbStatus(true);
                connection = MySqlConnectionFactory.getConnection();
                String query = "select uft_id from member_ext where member_id=?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, memberId);
                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    output.setDbObject(result.getString("uft_id"));
                }
                return output;
            } catch (Exception e) {
                e.printStackTrace();
                return output;
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                        return output;
                    } catch (SQLException e) {

                    }
                }
            }
        } else {
            output.setDbStatus(false);
        }
        return output;
    }

    public void insertPhoneNumberIntoTemp(String member_id, String phone, String option) {
        String phonenumber = phone.replaceAll("-", "");
        System.out.println("phonenumber: " + phonenumber);
        Connection connection = null;
        if (option.equalsIgnoreCase("no")) {
            phonenumber = "";
        }
        if (MySqlConnectionFactory.canConnect()) {
            try {
                String query = "insert into TEMP_UPDATE_PHONE (member_id,phone,phone_option) values(?,?,?) On DUPLICATE KEY UPDATE phone=?,phone_option=?";
                connection = MySqlConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, member_id);
                statement.setString(2, phonenumber);
                statement.setString(3, option);
                statement.setString(4, phonenumber);
                statement.setString(5, option);
                statement.execute();
            } catch (Exception e) {
                // e.printStackTrace();

            } finally {
                if (connection != null) {
                    try {
                        connection.close();

                    } catch (SQLException e) {

                    }
                }
            }
        }

    }

    public boolean isBatchRunning() {
        Connection connection = null;
        boolean status = false;
        if (MySqlConnectionFactory.canConnect()) {
            try {
                String query = "select value from status where name='mbs-sso-batch'";
                connection = MySqlConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    LOGGER.info("mbs-sso-batch is running" + "\r\n");
                    status = true;
                }

            } catch (Exception e) {
                return status;
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (Exception exception) {

                    }
                    return status;
                }
            }
        }
        return status;
    }

    public boolean insertEmailIntoTempTable(String emailAddress, String memberId) {
        LOGGER.info("insertEmailIntoTempTable(): put the updated email into Temp table" + "\r\n");
        Connection connection = null;
        boolean insert = false;
        if (MySqlConnectionFactory.canConnect()) {
            try {
                String query = "insert into TEMP_UPDATE_EMAIL (member_id,email) values(?,?) On DUPLICATE KEY UPDATE email=?,verified_email='F'";
                connection = MySqlConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, memberId);
                statement.setString(2, emailAddress);
                statement.setString(3, emailAddress);
                statement.execute();
                insert = true;
            } catch (Exception e) {
                // e.printStackTrace();

            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                        return insert;
                    } catch (SQLException e) {

                    }
                }
            }
        }
        return insert;
    }

    public String getDescriptionOfUnionEnrollmentStatus(String statusId) {
        Connection connection = null;
        String description = null;
        if (MySqlConnectionFactory.canConnect()) {
            try {
                String query = "select description from enrollment_status where enrollment_status_id = ?";
                connection = MySqlConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, statusId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    description = resultSet.getString("description");
                }
                return description;
            } catch (Exception e) {
                e.printStackTrace();
                return description;
            } finally {
                try {
                    connection.close();
                    return description;
                } catch (SQLException e) {

                }
            }
        }
        return null;
    }

    public RecordStatus existingWelfare(String memberId) {
        Connection connection = null;
        RecordStatus existing = new RecordStatus();
        if (MySqlConnectionFactory.canConnect()) {
            existing.setDbStatus(true);
            try {
                connection = MySqlConnectionFactory.getConnection();
                String query = "select welfare_status_id from welfare where member_id=?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, memberId);
                ResultSet resultset = statement.executeQuery();
                if (resultset.next()) {
                    String status = resultset.getString("welfare_status_id");
                    existing.setRecordStatus(status);
                }
            } catch (Exception e) {
                existing.setDbStatus(false);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                    return existing;
                } catch (Exception e) {

                }
            }
        } else {
            existing.setDbStatus(false);
            return existing;
        }
        return existing;
    }

    public RecordStatus existingUnion(String memberId) {
        Connection connection = null;
        RecordStatus existing = new RecordStatus();
        if (MySqlConnectionFactory.canConnect()) {

            existing.setDbStatus(true);
            try {
                connection = MySqlConnectionFactory.getConnection();
                String query = "select current_status from enrollment where member_id=? and DATE(`last_update`) = CURDATE()";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, memberId);
                ResultSet resultset = statement.executeQuery();
                if (resultset.next()) {
                    String status = resultset.getString("current_status");
                    existing.setRecordStatus(status);
                }
            } catch (Exception e) {
                existing.setDbStatus(false);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                    return existing;
                } catch (Exception e) {

                }
            }
        } else {
            existing.setDbStatus(false);
            return existing;
        }
        return existing;
    }

    public MemberExtData getMemberExtData(String memberId) {
        Connection connection = null;
        MemberExtData memberData = null;
        if (MySqlConnectionFactory.canConnect()) {
            try {
                String query = "select member_group memberGroup, last_name lastName, first_name firstName, ssn,zip,country,wf_covered,nonmember,enrollment_date enrollment_date,wf_enrollment_date wf_date,union_eligible union_eligible, certificate_eligible certificate_eligible from member_ext where member_id =?";
                connection = MySqlConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, memberId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String memberGroup = resultSet.getString("memberGroup") != null ? resultSet.getString("memberGroup")
                            : "";
                    String lastName = resultSet.getString("lastName");
                    String firstName = resultSet.getString("firstName");
                    String ssn = resultSet.getString("ssn");
                    ssn = BlowfishEncryption.decrypt(ssn);
                    String zip = resultSet.getString("zip") != null ? resultSet.getString("zip") : "";
                    String country = resultSet.getString("country") != null ? resultSet.getString("country") : "";
                    String wfCovered = resultSet.getString("wf_covered");
                    String nonmember = resultSet.getString("nonmember");
                    String union_eligible = resultSet.getString("union_eligible");
                    String certificate_eligible = resultSet.getString("certificate_eligible");
                    String wf_date = resultSet.getString("wf_date");
                    String enrollment_date = resultSet.getString("enrollment_date");
                    memberData = new MemberExtData(memberGroup, lastName, firstName, ssn, country, zip, wfCovered,
                            nonmember, union_eligible, certificate_eligible, wf_date, enrollment_date);
                }
                return memberData;
            } catch (Exception e) {
                e.printStackTrace();
                return memberData;
            } finally {
                try {
                    connection.close();
                    return memberData;
                } catch (SQLException e) {

                }
            }
        }
        return null;
    }

    public Boolean coveredByWelfare(String memberId) {
        Connection connection = null;
        Boolean coverd = false;
        if (MySqlConnectionFactory.canConnect()) {
            try {
                String query = "select wf_covered from member_ext where member_id=?";
                connection = MySqlConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, memberId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String coveredByWelfare = resultSet.getString("wf_covered");
                    if (coveredByWelfare != null) {
                        if (coveredByWelfare.equalsIgnoreCase("Y")) {
                            coverd = true;
                        } else {
                            coverd = false;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                    return coverd;
                } catch (SQLException e) {

                }
            }
        }
        return false;
    }

    public boolean insertOptinIntoMemberExt(String phone, String memberId) {
        LOGGER.info("insertEmailIntoTempTable(): put the updated email into Temp table" + "\r\n");
        Connection connection = null;
        boolean insert = false;
        if (MySqlConnectionFactory.canConnect()) {
            try {
                String query = "update member_ext set opt_in = ? where member_id=?";
                connection = MySqlConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, phone);
                statement.setString(2, memberId);
                statement.execute();
                insert = true;
            } catch (Exception e) {
                // e.printStackTrace();

            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                        return insert;
                    } catch (SQLException e) {

                    }
                }
            }
        }
        return insert;
    }

    public boolean getCommunityFlag(String memberId) {
        LOGGER.info("insertEmailIntoTempTable(): put the updated email into Temp table" + "\r\n");
        Connection connection = null;
        boolean allowFlag = false;
        if (MySqlConnectionFactory.canConnect()) {
            try {
                String query = "select query from salesforce_community_condition where community_name='memberCommunity'";
                connection = MySqlConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String conditionQuery = resultSet.getString("query");
                    PreparedStatement conditionStatement = connection.prepareStatement(conditionQuery);
                    conditionStatement.setString(1, memberId);
                    ResultSet conditionResultSet = conditionStatement.executeQuery();
                    if (conditionResultSet.next()) {
                        int allowCondition = conditionResultSet.getInt("allow_condition");
                        if (allowCondition == 1) {
                            allowFlag = true;
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;

            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                        return allowFlag;
                    } catch (SQLException e) {

                    }
                }
            }
        }
        return false;
    }

    public boolean existingMember(String memberId) {
        LOGGER.info("existingMember(): check if the user existing in the temp table" + "\r\n");
        Connection connection = null;
        boolean exist = false;
        try {
            String query = "select verified_email from TEMP_UPDATE_EMAIL where member_id=?";
            connection = MySqlConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, memberId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                exist = true;
                break;
            }

        } catch (Exception e) {
            // e.printStackTrace();

        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    return exist;
                } catch (SQLException e) {

                }
            }
        }
        return exist;
    }

    public void updateEmailStatus(String member_id) {
        LOGGER.info("updateEmailStatus(): update the temp table email status to true" + "\r\n");
        Connection connection = null;
        try {
            String query = "update TEMP_UPDATE_EMAIL set verified_email='T' where member_id=?";
            connection = MySqlConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, member_id);
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {

                }
            }
        }
    }

    public String getMemberIdBySSOId(String ssoId) {
        LOGGER.info("existingMember(): check if the user existing in the temp table" + "\r\n");
        Connection connection = null;
        String memberId = null;
        try {
            String query = "select member_id from sso_user_member where sso_id=?";
            connection = MySqlConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, ssoId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                memberId = resultSet.getString("member_id");
            }
            return memberId;
        } catch (Exception e) {
            e.printStackTrace();
            return memberId;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    return memberId;
                } catch (SQLException e) {

                }
            }
        }

    }

    // fetch active status from member extension
    public ObjectDB getActiveStatus(String memberId) {

        ObjectDB output = new ObjectDB();

        if (MySqlConnectionFactory.canConnect()) {
            output.setDbStatus(true);
            try (Connection connection = MySqlConnectionFactory.getConnection()) {
                String query = "select active_status from member_ext where member_id = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, memberId);
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        output.setDbObject(resultSet.getString("active_status"));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return output;
        } else {
            output.setDbStatus(false);
        }
        return output;
    }

    public ObjectDB getTitleId(String memberId) {
        ObjectDB output = new ObjectDB();

        if (MySqlConnectionFactory.canConnect()) {
            output.setDbStatus(true);
            try (Connection connection = MySqlConnectionFactory.getConnection()) {
                String query = "select title_id from member_ext where member_id = ? and title_id like 'TR%' and app_status = 'U';";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, memberId);
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        output.setDbObject(resultSet.getString("title_id"));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return output;
        } else {
            output.setDbStatus(false);
        }
        return output;
    }

    public ObjectDB getVerify(String memberId) {
        ObjectDB output = new ObjectDB();

        if (MySqlConnectionFactory.canConnect()) {
            output.setDbStatus(true);
            try (Connection connection = MySqlConnectionFactory.getConnection()) {
                String query = "select * from member_ext where member_id is not null;";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, memberId);
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        output.setDbObject(resultSet.getString("title_id"));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return output;
        } else {
            output.setDbStatus(false);
        }
        return output;
    }


}
