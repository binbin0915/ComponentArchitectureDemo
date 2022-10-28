package com.model.center.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.model.center.entity.User
import kotlinx.coroutines.flow.Flow


/**
 * 当您使用 Room 持久性库存储应用的数据时，您可以通过定义数据访问对象 (DAO) 与存储的数据进行交互。每个 DAO 都包含一些方法，这些方法提供对应用数据库的抽象访问权限。在编译时，Room 会自动为您定义的 DAO 生成实现。
 *
 * 通过使用 DAO（而不是查询构建器或直接查询）来访问应用的数据库，您可以使关注点保持分离，这是一项关键的架构原则。DAO 还可让您在测试应用时更轻松地模拟数据库访问。
 * - 您可以将每个 DAO 定义为一个接口或一个抽象类。对于基本用例，您通常应使用接口。
 * - 无论是哪种情况，您都必须始终使用 @Dao 为您的 DAO 添加注解。DAO 不具有属性，但它们定义了一个或多个方法，可用于与应用数据库中的数据进行交互。
 *
 * 有两种类型的 DAO 方法可以定义数据库。
 * - 便捷方法
 * ```kotlin
 * @Dao
 * interface UserDao {
 *      @Insert(onConflict = OnConflictStrategy.REPLACE)
 *      fun insertUsers(vararg users: User)
 *
 *      @Insert
 *      fun insertBothUsers(user1: User, user2: User)
 *
 *      @Insert
 *      fun insertUsersAndFriends(user: User, friends: List<User>)
 *
 *      @Update
 *      fun updateUsers(vararg users: User)
 *
 *      @Delete
 *      fun deleteUsers(vararg users: User)
 * }
 * ```
 * - 查询方法
 * ```kotlin
 * @Query("SELECT * FROM user")
 * fun loadAllUsers(): Array<User>
 * ```
 *
 *
 */
@Dao //注解Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(vararg users: User)//增

    @Delete
    fun deleteUser(user: User)//删除

    @Update
    fun updateUser(user: User)//修改

    @Query("SELECT * FROM user")
    suspend fun getAllUser(): List<User>//添加suspend关键字，异步操作

    @Query("SELECT * FROM user")
    fun getAllUserFlow(): Flow<List<User>>//表数据变化时，返回Flow

    @Query("SELECT * FROM user")
    fun getAllUserLiveData(): LiveData<List<User>>//表数据变化时，返回一个LiveData

    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user WHERE user_name LIKE :first LIMIT 1")
    fun findByUserName(first: String): User
}