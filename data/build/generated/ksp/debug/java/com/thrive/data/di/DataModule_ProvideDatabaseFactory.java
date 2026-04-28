package com.thrive.data.di;

import android.content.Context;
import com.thrive.data.local.ThriveDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class DataModule_ProvideDatabaseFactory implements Factory<ThriveDatabase> {
  private final Provider<Context> ctxProvider;

  public DataModule_ProvideDatabaseFactory(Provider<Context> ctxProvider) {
    this.ctxProvider = ctxProvider;
  }

  @Override
  public ThriveDatabase get() {
    return provideDatabase(ctxProvider.get());
  }

  public static DataModule_ProvideDatabaseFactory create(Provider<Context> ctxProvider) {
    return new DataModule_ProvideDatabaseFactory(ctxProvider);
  }

  public static ThriveDatabase provideDatabase(Context ctx) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.provideDatabase(ctx));
  }
}
