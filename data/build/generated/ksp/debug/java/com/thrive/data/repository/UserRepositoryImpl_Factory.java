package com.thrive.data.repository;

import com.thrive.data.local.UserDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class UserRepositoryImpl_Factory implements Factory<UserRepositoryImpl> {
  private final Provider<UserDao> daoProvider;

  public UserRepositoryImpl_Factory(Provider<UserDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public UserRepositoryImpl get() {
    return newInstance(daoProvider.get());
  }

  public static UserRepositoryImpl_Factory create(Provider<UserDao> daoProvider) {
    return new UserRepositoryImpl_Factory(daoProvider);
  }

  public static UserRepositoryImpl newInstance(UserDao dao) {
    return new UserRepositoryImpl(dao);
  }
}
