package metier;

import dao.DaoImpl;
import dao.IDao;
import org.junit.Assert;
import org.junit.Test;

public class MetierImplTest {

    @Test
    public void testCalculAvecDaoFictif() {
        // DAO fictif qui retourne 10.0
        IDao dao = new IDao() {
            @Override
            public double getValue() {
                return 10.0;
            }
        };

        MetierImpl metier = new MetierImpl();
        metier.setDao(dao);

        Assert.assertEquals(20.0, metier.calcul(), 0.001);
    }

    @Test
    public void testCalculAvecDaoImpl() {
        MetierImpl metier = new MetierImpl();
        metier.setDao(new DaoImpl());

        Assert.assertEquals(200.0, metier.calcul(), 0.001);
    }

    @Test(expected = IllegalStateException.class)
    public void testCalculSansDao() {
        new MetierImpl().calcul();
    }
}