package com.cronmetros.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import java.util.Random;

public class NotificationHelper {

    public static final String CH_MILESTONE     = "milestones";
    public static final String CH_ENCOURAGEMENT = "encouragement";

    private static final String[] MILESTONE_MSGS = {
        "Você chegou a %s com %s! Cada dia foi uma escolha sua. 🎉",
        "Marco atingido: %s em %s! Você é imparável! 🏆",
        "%s com %s! Orgulhe-se — você merece! ⭐",
        "Incrível! %s completos com %s! Continue assim! 🔥",
        "%s de %s! Uma conquista real. Não pare agora! 💪",
    };

    private static final String[] ENCOURAGEMENT_MSGS = {
        "Você está indo muito bem! Continue firme. 💪",
        "Cada dia conta. Orgulhe-se de si mesmo. ⭐",
        "Pequenos passos, grandes conquistas. Siga em frente! 🌟",
        "Você prova a si mesmo a cada dia que passa. 🏅",
        "A jornada é sua. Você está no controle! 🎯",
        "Lembre-se do motivo pelo qual começou. Você consegue! 🔥",
        "Um dia de cada vez. Você está fazendo história! 📅",
        "Seu esforço de hoje é sua vitória de amanhã. 💫",
        "Olha só quem está indo bem! Continue, você está arrasando. 🚀",
        "A consistência é o maior superpoder. Você tem ele! ✨",
    };

    public static void createChannels(Context ctx) {
        NotificationManager nm = ctx.getSystemService(NotificationManager.class);

        NotificationChannel milestoneChannel = new NotificationChannel(
                CH_MILESTONE, "Marcos e conquistas",
                NotificationManager.IMPORTANCE_HIGH);
        milestoneChannel.setDescription("Notificações ao atingir 1 dia, 1 semana, 1 mês...");
        milestoneChannel.enableVibration(true);
        milestoneChannel.setVibrationPattern(new long[]{0, 200, 100, 200, 100, 400});
        nm.createNotificationChannel(milestoneChannel);

        NotificationChannel encourageChannel = new NotificationChannel(
                CH_ENCOURAGEMENT, "Encorajamento",
                NotificationManager.IMPORTANCE_DEFAULT);
        encourageChannel.setDescription("Mensagens motivacionais periódicas");
        nm.createNotificationChannel(encourageChannel);
    }

    public static void showMilestoneNotification(Context ctx,
                                                  String emoji, String name,
                                                  String milestoneLabel,
                                                  int days, int notifId) {
        Random rng = new Random();
        String template = MILESTONE_MSGS[rng.nextInt(MILESTONE_MSGS.length)];
        String body = String.format(template, milestoneLabel, days + " dias");

        PendingIntent pi = PendingIntent.getActivity(ctx, 0,
                new Intent(ctx, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK),
                PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder b = new NotificationCompat.Builder(ctx, CH_MILESTONE)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(emoji + " " + name)
                .setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[]{0, 200, 100, 200, 100, 400})
                .setContentIntent(pi)
                .setAutoCancel(true);

        ctx.getSystemService(NotificationManager.class).notify(notifId, b.build());
    }

    public static void showEncouragementNotification(Context ctx,
                                                      String emoji, String name,
                                                      int days, int notifId) {
        Random rng = new Random();
        String body  = ENCOURAGEMENT_MSGS[rng.nextInt(ENCOURAGEMENT_MSGS.length)];
        String title = emoji + " " + name + " · " + days + (days == 1 ? " dia" : " dias");

        PendingIntent pi = PendingIntent.getActivity(ctx, 0,
                new Intent(ctx, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK),
                PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder b = new NotificationCompat.Builder(ctx, CH_ENCOURAGEMENT)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pi)
                .setAutoCancel(true);

        ctx.getSystemService(NotificationManager.class).notify(notifId, b.build());
    }
}
