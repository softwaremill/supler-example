package com.softwaremill.supler_example.dao.passwordResetCode

import com.softwaremill.supler_example.domain.PasswordResetCode

import scala.language.implicitConversions

trait PasswordResetCodeDao {

  def store(code: PasswordResetCode): Unit

  def load(code: String): Option[PasswordResetCode]

  def delete(code: PasswordResetCode): Unit
}