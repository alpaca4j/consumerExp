import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { INotification, Notification } from '../notification.model';
import { NotificationService } from '../service/notification.service';

@Component({
  selector: 'jhi-notification-update',
  templateUrl: './notification-update.component.html',
})
export class NotificationUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    time: [],
    origin: [],
    title: [],
    message: [],
    user: [],
    level: [],
    acknowledged: [],
  });

  constructor(protected notificationService: NotificationService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notification }) => {
      if (notification.id === undefined) {
        const today = dayjs().startOf('day');
        notification.time = today;
      }

      this.updateForm(notification);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const notification = this.createFromForm();
    if (notification.id !== undefined) {
      this.subscribeToSaveResponse(this.notificationService.update(notification));
    } else {
      this.subscribeToSaveResponse(this.notificationService.create(notification));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INotification>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(notification: INotification): void {
    this.editForm.patchValue({
      id: notification.id,
      time: notification.time ? notification.time.format(DATE_TIME_FORMAT) : null,
      origin: notification.origin,
      title: notification.title,
      message: notification.message,
      user: notification.user,
      level: notification.level,
      acknowledged: notification.acknowledged,
    });
  }

  protected createFromForm(): INotification {
    return {
      ...new Notification(),
      id: this.editForm.get(['id'])!.value,
      time: this.editForm.get(['time'])!.value ? dayjs(this.editForm.get(['time'])!.value, DATE_TIME_FORMAT) : undefined,
      origin: this.editForm.get(['origin'])!.value,
      title: this.editForm.get(['title'])!.value,
      message: this.editForm.get(['message'])!.value,
      user: this.editForm.get(['user'])!.value,
      level: this.editForm.get(['level'])!.value,
      acknowledged: this.editForm.get(['acknowledged'])!.value,
    };
  }
}
