package com.thrive.data.repository;

import com.thrive.data.local.SessionDao;
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
public final class SessionRepositoryImpl_Factory implements Factory<SessionRepositoryImpl> {
  private final Provider<SessionDao> daoProvider;

  public SessionRepositoryImpl_Factory(Provider<SessionDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public SessionRepositoryImpl get() {
    return newInstance(daoProvider.get());
  }

  public static SessionRepositoryImpl_Factory create(Provider<SessionDao> daoProvider) {
    return new SessionRepositoryImpl_Factory(daoProvider);
  }

  public static SessionRepositoryImpl newInstance(SessionDao dao) {
    return new SessionRepositoryImpl(dao);
  }
}
