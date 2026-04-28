package com.thrive.data.di;

import com.thrive.data.local.ThriveDatabase;
import com.thrive.data.local.UserDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DataModule_ProvideUserDaoFactory implements Factory<UserDao> {
  private final Provider<ThriveDatabase> dbProvider;

  public DataModule_ProvideUserDaoFactory(Provider<ThriveDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public UserDao get() {
    return provideUserDao(dbProvider.get());
  }

  public static DataModule_ProvideUserDaoFactory create(Provider<ThriveDatabase> dbProvider) {
    return new DataModule_ProvideUserDaoFactory(dbProvider);
  }

  public static UserDao provideUserDao(ThriveDatabase db) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.provideUserDao(db));
  }
}
