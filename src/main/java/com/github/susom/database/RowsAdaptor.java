/*
 * Copyright 2014 The Board of Trustees of The Leland Stanford Junior University.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.susom.database;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Safely wrap a ResultSet and provide access to the data it contains.
 *
 * @author garricko
 */
class RowsAdaptor implements Rows {
  private final ResultSet rs;
  private int column = 1;

  public RowsAdaptor(ResultSet rs) {
    this.rs = rs;
  }

  @Override
  public boolean next() {
    try {
      column = 1;
      return rs.next();
    } catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  @NotNull
  @Override
  public String[] getColumnNames() {
    try {
      ResultSetMetaData metaData = rs.getMetaData();
      String[] names = new String[metaData.getColumnCount()];
      for (int i = 0; i < names.length; i++) {
        names[i] = metaData.getColumnName(i + 1);
      }
      return names;
    } catch (SQLException e) {
      throw new DatabaseException("Unable to retrieve metadata from ResultSet", e);
    }
  }

  @Nullable
  @Override
  public Integer getIntegerOrNull() {
    return getIntegerOrNull(column++);
  }

  @Override
  public Integer getIntegerOrNull(int columnOneBased) {
    try {
      return toInteger(rs, columnOneBased);
    } catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  @Override
  public Integer getIntegerOrNull(String columnName) {
    try {
      return toInteger(rs, columnName);
    } catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  @Override
  public int getIntegerOrZero() {
    return getIntegerOrZero(column++);
  }

  @Override
  public int getIntegerOrZero(int columnOneBased) {
    Integer result = getIntegerOrNull(columnOneBased);
    if (result == null) {
      result = 0;
    }
    return result;
  }

  @Override
  public int getIntegerOrZero(String columnName) {
    Integer result = getIntegerOrNull(columnName);
    if (result == null) {
      result = 0;
    }
    return result;
  }

  @Nullable
  @Override
  public Long getLongOrNull() {
    return getLongOrNull(column++);
  }

  @Override
  public Long getLongOrNull(int column) {
    try {
      return toLong(rs, column);
    } catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  @Override
  public Long getLongOrNull(String columnName) {
    try {
      return toLong(rs, columnName);
    } catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  @Override
  public long getLongOrZero() {
    return getLongOrZero(column++);
  }

  @Override
  public long getLongOrZero(int columnOneBased) {
    Long result = getLongOrNull(columnOneBased);
    if (result == null) {
      result = 0L;
    }
    return result;
  }

  @Override
  public long getLongOrZero(String columnName) {
    Long result = getLongOrNull(columnName);
    if (result == null) {
      result = 0L;
    }
    return result;
  }

  @Nullable
  @Override
  public Float getFloatOrNull() {
    return getFloatOrNull(column++);
  }

  @Override
  public Float getFloatOrNull(int columnOneBased) {
    try {
      return toFloat(rs, columnOneBased);
    } catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  @Override
  public Float getFloatOrNull(String columnName) {
    try {
      return toFloat(rs, columnName);
    } catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  @Override
  public float getFloatOrZero() {
    return getFloatOrZero(column++);
  }

  @Override
  public float getFloatOrZero(int columnOneBased) {
    Float result = getFloatOrNull(columnOneBased);
    if (result == null) {
      result = 0f;
    }
    return result;
  }

  @Override
  public float getFloatOrZero(String columnName) {
    Float result = getFloatOrNull(columnName);
    if (result == null) {
      result = 0f;
    }
    return result;
  }

  @Nullable
  @Override
  public Double getDoubleOrNull() {
    return getDoubleOrNull(column++);
  }

  @Override
  public Double getDoubleOrNull(int columnOneBased) {
    try {
      return toDouble(rs, columnOneBased);
    } catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  @Override
  public Double getDoubleOrNull(String columnName) {
    try {
      return toDouble(rs, columnName);
    } catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  @Override
  public double getDoubleOrZero() {
    return getDoubleOrZero(column++);
  }

  @Override
  public double getDoubleOrZero(int columnOneBased) {
    Double result = getDoubleOrNull(columnOneBased);
    if (result == null) {
      result = 0d;
    }
    return result;
  }

  @Override
  public double getDoubleOrZero(String columnName) {
    Double result = getDoubleOrNull(columnName);
    if (result == null) {
      result = 0d;
    }
    return result;
  }

  @Nullable
  @Override
  public BigDecimal getBigDecimalOrNull() {
    return getBigDecimalOrNull(column++);
  }

  @Override
  public BigDecimal getBigDecimalOrNull(int columnOneBased) {
    try {
      return toBigDecimal(rs, columnOneBased);
    } catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  @Override
  public BigDecimal getBigDecimalOrNull(String columnName) {
    try {
      return toBigDecimal(rs, columnName);
    } catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  @NotNull
  @Override
  public BigDecimal getBigDecimalOrZero() {
    return getBigDecimalOrZero(column++);
  }

  @NotNull
  @Override
  public BigDecimal getBigDecimalOrZero(int columnOneBased) {
    BigDecimal result = getBigDecimalOrNull(columnOneBased);
    if (result == null) {
      result = BigDecimal.ZERO;
    }
    return result;
  }

  @NotNull
  @Override
  public BigDecimal getBigDecimalOrZero(String columnName) {
    BigDecimal result = getBigDecimalOrNull(columnName);
    if (result == null) {
      result = BigDecimal.ZERO;
    }
    return result;
  }

  @Nullable
  @Override
  public String getStringOrNull() {
    return getStringOrNull(column++);
  }

  @Override
  public String getStringOrNull(int columnOneBased) {
    try {
      String result = rs.getString(columnOneBased);
      if (result != null && result.length() == 0) {
        result = null;
      }
      return result;
    } catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  @Override
  public String getStringOrNull(String columnName) {
    try {
      String result = rs.getString(columnName);
      if (result != null && result.length() == 0) {
        result = null;
      }
      return result;
    } catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  @NotNull
  @Override
  public String getStringOrEmpty() {
    return getStringOrEmpty(column++);
  }

  @NotNull
  @Override
  public String getStringOrEmpty(int columnOneBased) {
    String result = getStringOrNull(columnOneBased);
    if (result == null) {
      result = "";
    }
    return result;
  }

  @NotNull
  @Override
  public String getStringOrEmpty(String columnName) {
    String result = getStringOrNull(columnName);
    if (result == null) {
      result = "";
    }
    return result;
  }

  @Nullable
  @Override
  public String getClobStringOrNull() {
    return getClobStringOrNull(column++);
  }

  @Override
  public String getClobStringOrNull(int columnOneBased) {
    try {
      String result = rs.getString(columnOneBased);
      if (result != null && result.length() == 0) {
        result = null;
      }
      return result;
    } catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  @Override
  public String getClobStringOrNull(String columnName) {
    try {
      String result = rs.getString(columnName);
      if (result != null && result.length() == 0) {
        result = null;
      }
      return result;
    } catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  @NotNull
  @Override
  public String getClobStringOrEmpty() {
    return getClobStringOrEmpty(column++);
  }

  @NotNull
  @Override
  public String getClobStringOrEmpty(int columnOneBased) {
    String result = getClobStringOrNull(columnOneBased);
    if (result == null) {
      result = "";
    }
    return result;
  }

  @NotNull
  @Override
  public String getClobStringOrEmpty(String columnName) {
    String result = getClobStringOrNull(columnName);
    if (result == null) {
      result = "";
    }
    return result;
  }

  @Nullable
  @Override
  public Reader getClobReaderOrNull() {
    return getClobReaderOrNull(column++);
  }

  @Override
  public Reader getClobReaderOrNull(int columnOneBased) {
    try {
      return rs.getCharacterStream(columnOneBased);
    } catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  @Override
  public Reader getClobReaderOrNull(String columnName) {
    try {
      return rs.getCharacterStream(columnName);
    } catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  @NotNull
  @Override
  public Reader getClobReaderOrEmpty() {
    return getClobReaderOrEmpty(column++);
  }

  @NotNull
  @Override
  public Reader getClobReaderOrEmpty(int columnOneBased) {
    Reader result = getClobReaderOrNull(columnOneBased);
    if (result == null) {
      result = new StringReader("");
    }
    return result;
  }

  @NotNull
  @Override
  public Reader getClobReaderOrEmpty(String columnName) {
    Reader result = getClobReaderOrNull(columnName);
    if (result == null) {
      result = new StringReader("");
    }
    return result;
  }

  @Nullable
  @Override
  public byte[] getBlobBytesOrNull() {
    return getBlobBytesOrNull(column++);
  }

  @Override
  public byte[] getBlobBytesOrNull(int columnOneBased) {
    try {
      return rs.getBytes(columnOneBased);
    } catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  @Override
  public byte[] getBlobBytesOrNull(String columnName) {
    try {
      return rs.getBytes(columnName);
    } catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  @NotNull
  @Override
  public byte[] getBlobBytesOrZeroLen() {
    return getBlobBytesOrZeroLen(column++);
  }

  @NotNull
  @Override
  public byte[] getBlobBytesOrZeroLen(int columnOneBased) {
    byte[] result = getBlobBytesOrNull(columnOneBased);
    if (result == null) {
      result = new byte[0];
    }
    return result;
  }

  @NotNull
  @Override
  public byte[] getBlobBytesOrZeroLen(String columnName) {
    byte[] result = getBlobBytesOrNull(columnName);
    if (result == null) {
      result = new byte[0];
    }
    return result;
  }

  @Nullable
  @Override
  public InputStream getBlobInputStreamOrNull() {
    return getBlobInputStreamOrNull(column++);
  }

  @Override
  public InputStream getBlobInputStreamOrNull(int columnOneBased) {
    try {
      return rs.getBinaryStream(columnOneBased);
    } catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  @Override
  public InputStream getBlobInputStreamOrNull(String columnName) {
    try {
      return rs.getBinaryStream(columnName);
    } catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  @NotNull
  @Override
  public InputStream getBlobInputStreamOrEmpty() {
    return getBlobInputStreamOrEmpty(column++);
  }

  @NotNull
  @Override
  public InputStream getBlobInputStreamOrEmpty(int columnOneBased) {
    InputStream result = getBlobInputStreamOrNull(columnOneBased);
    if (result == null) {
      result = new ByteArrayInputStream(new byte[0]);
    }
    return result;
  }

  @NotNull
  @Override
  public InputStream getBlobInputStreamOrEmpty(String columnName) {
    InputStream result = getBlobInputStreamOrNull(columnName);
    if (result == null) {
      result = new ByteArrayInputStream(new byte[0]);
    }
    return result;
  }

  @Nullable
  @Override
  public Date getDateOrNull() {
    return getDateOrNull(column++);
  }

  @Override
  public Date getDateOrNull(int columnOneBased) {
    try {
      return toDate(rs, columnOneBased);
    } catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  @Override
  public Date getDateOrNull(String columnName) {
    try {
      return toDate(rs, columnName);
    } catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  /**
   * Make sure the Timestamp will return getTime() accurate to the millisecond
   * (if possible) and truncate away nanoseconds.
   */
  private Date timestampToDate(Timestamp ts) {
    long millis = ts.getTime();
    int nanos = ts.getNanos();
    return new Date(millis / 1000 * 1000 + nanos / 1000000);
  }

  private Date toDate(ResultSet rs, int col) throws SQLException {
    Timestamp val = rs.getTimestamp(col);
    return val == null ? null : timestampToDate(val);
  }

  private Date toDate(ResultSet rs, String col) throws SQLException {
    Timestamp val = rs.getTimestamp(col);
    return val == null ? null : timestampToDate(val);
  }

  private Integer toInteger(ResultSet rs, int col) throws SQLException {
    int val = rs.getInt(col);
    return rs.wasNull() ? null : val;
  }

  private Integer toInteger(ResultSet rs, String col) throws SQLException {
    int val = rs.getInt(col);
    return rs.wasNull() ? null : val;
  }

  private Long toLong(ResultSet rs, int col) throws SQLException {
    long val = rs.getLong(col);
    return rs.wasNull() ? null : val;
  }

  private Long toLong(ResultSet rs, String col) throws SQLException {
    long val = rs.getLong(col);
    return rs.wasNull() ? null : val;
  }

  private Float toFloat(ResultSet rs, int col) throws SQLException {
    float val = rs.getFloat(col);
    return rs.wasNull() ? null : val;
  }

  private Float toFloat(ResultSet rs, String col) throws SQLException {
    float val = rs.getFloat(col);
    return rs.wasNull() ? null : val;
  }

  private Double toDouble(ResultSet rs, int col) throws SQLException {
    double val = rs.getDouble(col);
    return rs.wasNull() ? null : val;
  }

  private Double toDouble(ResultSet rs, String col) throws SQLException {
    double val = rs.getDouble(col);
    return rs.wasNull() ? null : val;
  }

  private BigDecimal fixBigDecimal(BigDecimal val) {
    if (val.scale() > 0) {
      val = val.stripTrailingZeros();
      if (val.scale() < 0) {
        val = val.setScale(0);
      }
    }
    return val;
  }

  private BigDecimal toBigDecimal(ResultSet rs, int col) throws SQLException {
    BigDecimal val = rs.getBigDecimal(col);
    return val == null ? null : fixBigDecimal(val);
  }

  private BigDecimal toBigDecimal(ResultSet rs, String col) throws SQLException {
    BigDecimal val = rs.getBigDecimal(col);
    return val == null ? null : fixBigDecimal(val);
  }
}
