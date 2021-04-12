import * as dayjs from 'dayjs';

export interface INotification {
  id?: number;
  time?: dayjs.Dayjs | null;
  origin?: string | null;
  title?: string | null;
  message?: string | null;
  user?: string | null;
  level?: string | null;
  acknowledged?: boolean | null;
}

export class Notification implements INotification {
  constructor(
    public id?: number,
    public time?: dayjs.Dayjs | null,
    public origin?: string | null,
    public title?: string | null,
    public message?: string | null,
    public user?: string | null,
    public level?: string | null,
    public acknowledged?: boolean | null
  ) {
    this.acknowledged = this.acknowledged ?? false;
  }
}

export function getNotificationIdentifier(notification: INotification): number | undefined {
  return notification.id;
}
