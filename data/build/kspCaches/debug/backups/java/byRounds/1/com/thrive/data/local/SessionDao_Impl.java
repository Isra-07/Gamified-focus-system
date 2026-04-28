package com.thrive.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SessionDao_Impl implements SessionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SessionEntity> __insertionAdapterOfSessionEntity;

  public SessionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSessionEntity = new EntityInsertionAdapter<SessionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `sessions` (`id`,`startTime`,`targetSeconds`,`actualSeconds`,`xpEarned`,`distractionCount`,`focusRate`,`completedAt`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SessionEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindLong(2, entity.getStartTime());
        statement.bindLong(3, entity.getTargetSeconds());
        statement.bindLong(4, entity.getActualSeconds());
        statement.bindLong(5, entity.getXpEarned());
        statement.bindLong(6, entity.getDistractionCount());
        statement.bindDouble(7, entity.getFocusRate());
        statement.bindString(8, entity.getCompletedAt());
      }
    };
  }

  @Override
  public Object save(final SessionEntity session, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSessionEntity.insert(session);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<SessionEntity>> observeAll() {
    final String _sql = "SELECT * FROM sessions ORDER BY startTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sessions"}, new Callable<List<SessionEntity>>() {
      @Override
      @NonNull
      public List<SessionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfTargetSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "targetSeconds");
          final int _cursorIndexOfActualSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "actualSeconds");
          final int _cursorIndexOfXpEarned = CursorUtil.getColumnIndexOrThrow(_cursor, "xpEarned");
          final int _cursorIndexOfDistractionCount = CursorUtil.getColumnIndexOrThrow(_cursor, "distractionCount");
          final int _cursorIndexOfFocusRate = CursorUtil.getColumnIndexOrThrow(_cursor, "focusRate");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
          final List<SessionEntity> _result = new ArrayList<SessionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SessionEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final int _tmpTargetSeconds;
            _tmpTargetSeconds = _cursor.getInt(_cursorIndexOfTargetSeconds);
            final int _tmpActualSeconds;
            _tmpActualSeconds = _cursor.getInt(_cursorIndexOfActualSeconds);
            final int _tmpXpEarned;
            _tmpXpEarned = _cursor.getInt(_cursorIndexOfXpEarned);
            final int _tmpDistractionCount;
            _tmpDistractionCount = _cursor.getInt(_cursorIndexOfDistractionCount);
            final float _tmpFocusRate;
            _tmpFocusRate = _cursor.getFloat(_cursorIndexOfFocusRate);
            final String _tmpCompletedAt;
            _tmpCompletedAt = _cursor.getString(_cursorIndexOfCompletedAt);
            _item = new SessionEntity(_tmpId,_tmpStartTime,_tmpTargetSeconds,_tmpActualSeconds,_tmpXpEarned,_tmpDistractionCount,_tmpFocusRate,_tmpCompletedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getTotalXp(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COALESCE(SUM(xpEarned), 0) FROM sessions";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
