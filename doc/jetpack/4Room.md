#### 3.Room
##### 定义：Room是一个对象关系映射(ORM)库。Room抽象了SQLite的使用，可以在充分利用SQLite的同时访问流畅的数据库。我理解为SQLite的封装。
##### 用法：(如果在项目中已经使用SQlite就不要变更Room,如果项目中没有使用，建议使用Room)
- 创建实体类
- 创建DAO
- 继承RoomDatabase

```
//@Entity 实体类
@Entity(tableName = "products")
public class ProductEntity implements Product {
    @PrimaryKey
    private int id;
    private String name;
    private String description;
    private int price;
```


```
//@Dao注解，接口
@Dao
public interface ProductDao {
    @Query("SELECT * FROM products")
    LiveData<List<ProductEntity>> loadAllProducts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ProductEntity> products);

    @Query("select * from products where id = :productId")
    LiveData<ProductEntity> loadProduct(int productId);

    @Query("select * from products where id = :productId")
    ProductEntity loadProductSync(int productId);

    @Query("SELECT products.* FROM products JOIN productsFts ON (products.id = productsFts.rowid) "
        + "WHERE productsFts MATCH :query")
    LiveData<List<ProductEntity>> searchAllProducts(String query);
}
```


```
//@Database 注解 抽象类
@Database(entities = {ProductEntity.class, ProductFtsEntity.class, CommentEntity.class}, version = 2)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase sInstance;

    @VisibleForTesting
    public static final String DATABASE_NAME = "basic-sample-db";

    public abstract ProductDao productDao();

    public abstract CommentDao commentDao();
```



##### 参考链接 [link](https://www.jianshu.com/p/3e358eb9ac43)