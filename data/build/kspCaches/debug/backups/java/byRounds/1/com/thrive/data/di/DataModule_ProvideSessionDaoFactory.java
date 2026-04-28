package com.thrive.data.di;

import com.thrive.data.local.SessionDao;
import com.thrive.data.local.ThriveDatabase;
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
public final class DataModule_ProvideSessionDaoFactory implements Factory<SessionDao> {
  private final Provider<ThriveDatabase> dbProvider;

  public DataModule_ProvideSessionDaoFactory(Provider<ThriveDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public SessionDao get() {
    return provideSessionDao(dbProvider.get());
  }

  public static DataModule_ProvideSessionDaoFactory create(Provider<ThriveDatabase> dbProvider) {
    return new DataModule_ProvideSessionDaoFactory(dbProvider);
  }

  public static SessionDao provideSessionDao(ThriveDatabase db) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.provideSessionDao(db));
  }
}
