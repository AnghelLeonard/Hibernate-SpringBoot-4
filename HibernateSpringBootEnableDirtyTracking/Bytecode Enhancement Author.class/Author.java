package com.bookstore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import java.io.Serializable;
import java.util.Objects;
import org.hibernate.bytecode.enhance.internal.tracker.DirtyTracker;
import org.hibernate.bytecode.enhance.internal.tracker.NoopCollectionTracker;
import org.hibernate.bytecode.enhance.internal.tracker.SimpleFieldTracker;
import org.hibernate.bytecode.enhance.spi.CollectionTracker;
import org.hibernate.bytecode.enhance.spi.EnhancementInfo;
import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.engine.spi.ManagedEntity;
import org.hibernate.engine.spi.SelfDirtinessTracker;
import org.hibernate.internal.util.collections.ArrayHelper;

@Entity
@EnhancementInfo(
   version = "7.0.10.Final"
)
public class Author implements Serializable, ManagedEntity, SelfDirtinessTracker {
   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(
      strategy = GenerationType.IDENTITY
   )
   Long id;
   String name;
   String genre;
   int age;
   @Transient
   private transient EntityEntry $$_hibernate_entityEntryHolder;
   @Transient
   private transient ManagedEntity $$_hibernate_previousManagedEntity;
   @Transient
   private transient ManagedEntity $$_hibernate_nextManagedEntity;
   @Transient
   private transient boolean $$_hibernate_useTracker;
   @Transient
   private transient int $$_hibernate_instanceId;
   @Transient
   private transient DirtyTracker $$_hibernate_tracker;

   public Long getId() {
      return this.$$_hibernate_read_id();
   }

   public void setId(Long id) {
      this.$$_hibernate_write_id(id);
   }

   public String getName() {
      return this.$$_hibernate_read_name();
   }

   public void setName(String name) {
      this.$$_hibernate_write_name(name);
   }

   public String getGenre() {
      return this.$$_hibernate_read_genre();
   }

   public void setGenre(String genre) {
      this.$$_hibernate_write_genre(genre);
   }

   public int getAge() {
      return this.$$_hibernate_read_age();
   }

   public void setAge(int age) {
      this.$$_hibernate_write_age(age);
   }

   public Object $$_hibernate_getEntityInstance() {
      return this;
   }

   public EntityEntry $$_hibernate_getEntityEntry() {
      return this.$$_hibernate_entityEntryHolder;
   }

   public void $$_hibernate_setEntityEntry(EntityEntry var1) {
      this.$$_hibernate_entityEntryHolder = var1;
   }

   public ManagedEntity $$_hibernate_getPreviousManagedEntity() {
      return this.$$_hibernate_previousManagedEntity;
   }

   public void $$_hibernate_setPreviousManagedEntity(ManagedEntity var1) {
      this.$$_hibernate_previousManagedEntity = var1;
   }

   public ManagedEntity $$_hibernate_getNextManagedEntity() {
      return this.$$_hibernate_nextManagedEntity;
   }

   public void $$_hibernate_setNextManagedEntity(ManagedEntity var1) {
      this.$$_hibernate_nextManagedEntity = var1;
   }

   public boolean $$_hibernate_useTracker() {
      return this.$$_hibernate_useTracker;
   }

   public void $$_hibernate_setUseTracker(boolean var1) {
      this.$$_hibernate_useTracker = var1;
   }

   public int $$_hibernate_getInstanceId() {
      return this.$$_hibernate_instanceId;
   }

   public void $$_hibernate_setInstanceId(int var1) {
      this.$$_hibernate_instanceId = var1;
   }

   public EntityEntry $$_hibernate_setPersistenceInfo(EntityEntry var1, ManagedEntity var2, ManagedEntity var3, int var4) {
      EntityEntry var5 = null;
      var5 = this.$$_hibernate_entityEntryHolder;
      this.$$_hibernate_entityEntryHolder = var1;
      this.$$_hibernate_previousManagedEntity = var2;
      this.$$_hibernate_nextManagedEntity = var3;
      this.$$_hibernate_instanceId = var4;
      return var5;
   }

   public void $$_hibernate_trackChange(String var1) {
      if (this.$$_hibernate_tracker == null) {
         this.$$_hibernate_tracker = new SimpleFieldTracker();
      }

      this.$$_hibernate_tracker.add(var1);
   }

   public String[] $$_hibernate_getDirtyAttributes() {
      String[] var1 = null;
      var1 = this.$$_hibernate_tracker == null ? ArrayHelper.EMPTY_STRING_ARRAY : this.$$_hibernate_tracker.get();
      return var1;
   }

   public boolean $$_hibernate_hasDirtyAttributes() {
      boolean var1 = false;
      var1 = this.$$_hibernate_tracker != null && !this.$$_hibernate_tracker.isEmpty();
      return var1;
   }

   public void $$_hibernate_clearDirtyAttributes() {
      if (this.$$_hibernate_tracker != null) {
         this.$$_hibernate_tracker.clear();
      }

   }

   public void $$_hibernate_suspendDirtyTracking(boolean var1) {
      if (this.$$_hibernate_tracker == null) {
         this.$$_hibernate_tracker = new SimpleFieldTracker();
      }

      this.$$_hibernate_tracker.suspend(var1);
   }

   public CollectionTracker $$_hibernate_getCollectionTracker() {
      CollectionTracker var1 = null;
      var1 = NoopCollectionTracker.INSTANCE;
      return var1;
   }

   public Long $$_hibernate_read_id() {
      return this.id;
   }

   public void $$_hibernate_write_id(Long var1) {
      this.id = var1;
   }

   public String $$_hibernate_read_name() {
      return this.name;
   }

   public void $$_hibernate_write_name(String var1) {
      if (!Objects.deepEquals(var1, this.name)) {
         this.$$_hibernate_trackChange("name");
      }

      this.name = var1;
   }

   public String $$_hibernate_read_genre() {
      return this.genre;
   }

   public void $$_hibernate_write_genre(String var1) {
      if (!Objects.deepEquals(var1, this.genre)) {
         this.$$_hibernate_trackChange("genre");
      }

      this.genre = var1;
   }

   public int $$_hibernate_read_age() {
      return this.age;
   }

   public void $$_hibernate_write_age(int var1) {
      if (var1 - this.age != 0) {
         this.$$_hibernate_trackChange("age");
      }

      this.age = var1;
   }
}
