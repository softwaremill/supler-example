package com.softwaremill.supler_example.dao

import com.softwaremill.supler_example.dao.passwordResetCode.SqlPasswordResetCodeDao
import com.softwaremill.supler_example.dao.person.SqlPersonDao
import com.softwaremill.supler_example.dao.sql.SqlDatabase
import com.softwaremill.supler_example.dao.user.SqlUserDao

trait Daos {
  lazy val userDao = new SqlUserDao(sqlDatabase)

  lazy val codeDao = new SqlPasswordResetCodeDao(sqlDatabase)

  lazy val personDao = new SqlPersonDao(sqlDatabase)

  def sqlDatabase: SqlDatabase
}
