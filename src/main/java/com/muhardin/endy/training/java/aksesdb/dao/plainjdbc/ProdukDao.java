package com.muhardin.endy.training.java.aksesdb.dao.plainjdbc;

import com.muhardin.endy.training.java.aksesdb.domain.Produk;
import com.muhardin.endy.training.java.aksesdb.service.plainjdbc.PagingHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProdukDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProdukDao.class);
    private static final String SQL_INSERT_PRODUK = "insert into m_produk (kode, nama, harga) values (?,?,?)";
    private static final String SQL_UPDATE_PRODUK = "update m_produk set kode = ?, nama = ?, harga = ? where id = ?";
    private static final String SQL_HAPUS_PRODUK = "delete from m_produk where id = ?";
    private static final String SQL_CARI_PRODUK_BY_ID = "select * from m_produk where id = ?";
    private static final String SQL_CARI_PRODUK_BY_KODE = "select * from m_produk where kode = ?";
    private static final String SQL_HITUNG_SEMUA_PRODUK = "select count(*) from m_produk";
    private static final String SQL_CARI_SEMUA_PRODUK = "select * from m_produk limit ?,?";
    private static final String SQL_HITUNG_PRODUK_BY_NAMA = "select count(*) from m_produk where lower(nama) like ?";
    private static final String SQL_CARI_PRODUK_BY_NAMA = "select * from m_produk where lower(nama) like ? limit ?,?";
    
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public void simpan(Produk p) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            
            if(p.getId() == null) {
            
                PreparedStatement ps = conn.prepareStatement(SQL_INSERT_PRODUK, Statement.RETURN_GENERATED_KEYS);

                ps.setString(1, p.getKode());
                ps.setString(2, p.getNama());
                ps.setBigDecimal(3, p.getHarga());

                ps.executeUpdate();

                ResultSet rsKey = ps.getGeneratedKeys();
                if(rsKey.next()) {
                    p.setId(((Long) rsKey.getObject(1)).intValue());
                }
            } else {
                PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_PRODUK);

                ps.setString(1, p.getKode());
                ps.setString(2, p.getNama());
                ps.setBigDecimal(3, p.getHarga());
                ps.setInt(4, p.getId());

                ps.executeUpdate();
            }
            
            conn.commit();
            conn.setAutoCommit(true);
        } catch (Exception err) {
            LOGGER.error(err.getMessage(), err);
            if(conn !=  null){
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        } finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }
    }

    public void hapus(Produk p) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(SQL_HAPUS_PRODUK);
            
            ps.setInt(1, p.getId());
            ps.executeUpdate();
            
            conn.commit();
            conn.setAutoCommit(true);
        } catch (Exception err) {
            LOGGER.error(err.getMessage(), err);
            if(conn !=  null){
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        } finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }
    }

    public Produk cariProdukById(Integer id) {
        Connection conn = null;
        Produk p = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(SQL_CARI_PRODUK_BY_ID);
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                p = konversiResultSetJadiProduk(rs);
            }
            
            conn.commit();
            conn.setAutoCommit(true);
        } catch (Exception err) {
            LOGGER.error(err.getMessage(), err);
            if(conn !=  null){
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        } finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
            return p;
        }
    }

    public Produk cariProdukByKode(String kode) {
        Connection conn = null;
        Produk p = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(SQL_CARI_PRODUK_BY_KODE);
            
            ps.setString(1, kode);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                p = konversiResultSetJadiProduk(rs);
            }
            
            conn.commit();
            conn.setAutoCommit(true);
        } catch (Exception err) {
            LOGGER.error(err.getMessage(), err);
            if(conn !=  null){
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        } finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
            return p;
        }
    }

    public Long hitungSemuaProduk() {
        Connection conn = null;
        Long p = 0L;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(SQL_HITUNG_SEMUA_PRODUK);
            
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                p = rs.getLong(1);
            }
            
            conn.commit();
            conn.setAutoCommit(true);
        } catch (Exception err) {
            LOGGER.error(err.getMessage(), err);
            if(conn !=  null){
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        } finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
            return p;
        }
    }

    public List<Produk> cariSemuaProduk(Integer halaman, Integer baris) {
        Connection conn = null;
        List<Produk> hasil = new ArrayList<Produk>();
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(SQL_CARI_SEMUA_PRODUK);
            ps.setInt(1, PagingHelper.halamanJadiStart(halaman, baris));
            ps.setInt(2, baris);
            
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Produk x = konversiResultSetJadiProduk(rs);
                hasil.add(x);
            }
            
            conn.commit();
            conn.setAutoCommit(true);
        } catch (Exception err) {
            LOGGER.error(err.getMessage(), err);
            if(conn !=  null){
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        } finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
            return hasil;
        }
    }

    public Long hitungProdukByNama(String nama) {
        Connection conn = null;
        Long p = 0L;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(SQL_HITUNG_PRODUK_BY_NAMA);
            ps.setString(1, "%"+nama.toLowerCase()+"%");
            
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                p = rs.getLong(1);
            }
            
            conn.commit();
            conn.setAutoCommit(true);
        } catch (Exception err) {
            LOGGER.error(err.getMessage(), err);
            if(conn !=  null){
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        } finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
            return p;
        }
    }

    public List<Produk> cariProdukByNama(String nama, Integer halaman, Integer baris) {
        Connection conn = null;
        List<Produk> hasil = new ArrayList<Produk>();
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(SQL_CARI_PRODUK_BY_NAMA);
            ps.setString(1, "%"+nama.toLowerCase()+"%");
            ps.setInt(2, PagingHelper.halamanJadiStart(halaman, baris));
            ps.setInt(3, baris);
            
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Produk x = konversiResultSetJadiProduk(rs);
                hasil.add(x);
            }
            
            conn.commit();
            conn.setAutoCommit(true);
        } catch (Exception err) {
            LOGGER.error(err.getMessage(), err);
            if(conn !=  null){
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        } finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
            return hasil;
        }
    }
    
    private Produk konversiResultSetJadiProduk(ResultSet rs) throws SQLException {
        Produk p = new Produk();
        p.setId((Integer) rs.getObject("id"));
        p.setKode(rs.getString("kode"));
        p.setNama(rs.getString("nama"));
        p.setHarga(rs.getBigDecimal("harga"));
        return p;
    }

}
