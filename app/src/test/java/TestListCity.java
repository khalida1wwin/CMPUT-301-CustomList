import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.simpleparadox.listycity.City;
import com.example.simpleparadox.listycity.CustomList;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class TestListCity {
    private CustomList list;
    @Before
    public void createList(){
        list = new CustomList(null, new ArrayList<City>());
    }
    @Test
    public void addCityTest(){
        int listSize = list.getCount();
        list.addCity(new City("Halifax","NS"));
        assertEquals(list.getCount(), listSize+1);
    }

    @Test
    public void hasCityTest(){
        City testCity = new City("Halifax","NS");
        list.addCity(testCity);
        assertTrue((list.hasCity(testCity)));

    }

    @Test
    public void deleteCityTest(){
        City testCity = new City("Edmonton","AB");
        list.addCity(testCity);
        int sizeBefore = list.getCount();
        list.deleteCity(testCity);
        assertEquals(list.getCount(),sizeBefore-1);
        assertFalse((list.hasCity(testCity)));
    }

    @Test
    public void countCitiesTest(){
        City testCity = new City("Edmonton","AB");
        list.addCity(testCity);
        int sizeBefore = list.countCities();
        list.deleteCity(testCity);
        assertEquals(list.getCount(),sizeBefore-1);
    }

}
