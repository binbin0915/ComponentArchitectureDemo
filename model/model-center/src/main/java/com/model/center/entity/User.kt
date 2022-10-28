package com.model.center.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * 当您使用 Room 持久性库存储应用数据时，您可以定义实体来表示要存储的对象。每个实体都对应于关联的 Room 数据库中的一个表，并且实体的每个实例都表示相应表中的一行数据。
 * 这意味着您可以使用 Room 实体定义数据库架构，而无需编写任何 SQL 代码。
 *
 * - 您可以将每个 Room 实体定义为带有 @Entity 注解的类。Room 实体包含数据库中相应表中的每一列的字段，包括构成主键的一个或多个列。
 * - 每个 Room 实体都必须定义一个主键，用于唯一标识相应数据库表中的每一行。执行此操作的最直接方式是使用 @PrimaryKey 为单个列添加注解。
 * - 如果您需要通过多个列的组合对实体实例进行唯一标识，则可以通过列出 @Entity 的 primaryKeys 属性中的以下列定义一个复合主键。
 * - 默认情况下，Room 会为实体中定义的每个字段创建一个列。 如果某个实体中有您不想保留的字段，则可以使用 @Ignore 为这些字段添加注解。如果实体继承了父实体的字段，则使用 @Entity 属性的 ignoredColumns 属性通常会更容易
 * - 在 Room 2.1.0 及更高版本中，您可以将基于 Java 的不可变值类（使用 @AutoValue 进行注解）用作应用数据库中的实体。此支持在实体的两个实例被视为相等（如果这两个实例的列包含相同的值）时尤为有用。
 * 将带有 @AutoValue 注解的类用作实体时，您可以使用 @PrimaryKey、@ColumnInfo、@Embedded 和 @Relation 为该类的抽象方法添加注解。但是，您必须在每次使用这些注解时添加 @CopyAnnotations 注解，以便 Room 可以正确解释这些方法的自动生成实现。
 */
@Entity(tableName = "user")//表名
class User {

    @Ignore
    constructor(id: Int) {
        this.id = id
    }

    @Ignore
    constructor(userName: String, age: Int) {
        this.userName = userName
        this.age = age
    }

    constructor(id: Int, userName: String, age: Int) {
        this.id = id
        this.userName = userName
        this.age = age
    }

    @PrimaryKey(autoGenerate = true)//自增长的主键
    @ColumnInfo(name = "id")
    var id: Int = 0//typeAffinity，指定类型，name指定行名称

    @ColumnInfo(name = "user_name", typeAffinity = ColumnInfo.TEXT)
    var userName: String? = null

    @ColumnInfo(name = "age", typeAffinity = ColumnInfo.INTEGER)
    var age: Int = 0

    override fun toString(): String {
        return "User(id=$id, userName=$userName, age=$age)"
    }
}