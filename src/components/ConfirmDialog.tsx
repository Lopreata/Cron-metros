import React from 'react';
import { Dialog, Portal, Button, Text } from 'react-native-paper';

interface Props {
  visible: boolean;
  title: string;
  message: string;
  confirmLabel?: string;
  onConfirm: () => void;
  onDismiss: () => void;
  destructive?: boolean;
}

export function ConfirmDialog({
  visible,
  title,
  message,
  confirmLabel = 'Confirmar',
  onConfirm,
  onDismiss,
  destructive = false,
}: Props) {
  return (
    <Portal>
      <Dialog visible={visible} onDismiss={onDismiss} style={{ backgroundColor: '#1E1E2E' }}>
        <Dialog.Title style={{ color: '#CDD6F4' }}>{title}</Dialog.Title>
        <Dialog.Content>
          <Text style={{ color: '#A6ADC8' }}>{message}</Text>
        </Dialog.Content>
        <Dialog.Actions>
          <Button textColor="#6C7086" onPress={onDismiss}>Cancelar</Button>
          <Button
            textColor={destructive ? '#F38BA8' : '#CBA6F7'}
            onPress={() => { onConfirm(); onDismiss(); }}
          >
            {confirmLabel}
          </Button>
        </Dialog.Actions>
      </Dialog>
    </Portal>
  );
}
