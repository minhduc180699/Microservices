import { CardModel } from '@/shared/cards/card.model';
import { SignalTodayIssueModel } from '@/core/home/signals/signal-today-issue/signal-today-issue.model';

export class CardSignalIssueModel extends CardModel {
  signalIssue: SignalTodayIssueModel;
}
