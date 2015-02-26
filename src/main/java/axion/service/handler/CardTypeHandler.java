package axion.service.handler;

import axion.domain.Card;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CardTypeHandler extends BaseTypeHandler<Card> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Card card, JdbcType jdbcType) throws SQLException {
        if (jdbcType != null) {
            ps.setObject(i, card.getClass().getName(), jdbcType.TYPE_CODE);
        } else {
            ps.setString(i, card.getClass().getName());
        }
    }

    private Card getNullableResultThing(Object thing, Object columnThing) {
        String value = null;
        try {
            value = (String)thing.getClass().getMethod("getString",
                    columnThing instanceof Integer ? Integer.TYPE : String.class).invoke(thing, columnThing);
            if (value == null) {
                return null;
            }
            Class<? extends Card> cardClass = (Class<? extends Card>)Class.forName(value);
            return cardClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Unable to get card for class: " + value, e);
        }
    }

    @Override
    public Card getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return getNullableResultThing(rs, columnName);
    }

    @Override
    public Card getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return getNullableResultThing(rs, columnIndex);
    }

    @Override
    public Card getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getNullableResultThing(cs, columnIndex);
    }
}
