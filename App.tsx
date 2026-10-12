import React from 'react';
import { PaperProvider, MD3DarkTheme } from 'react-native-paper';
import { SafeAreaProvider } from 'react-native-safe-area-context';
import { HomeScreen } from './src/screens/HomeScreen';

const theme = {
  ...MD3DarkTheme,
  colors: {
    ...MD3DarkTheme.colors,
    primary: '#CBA6F7',
    secondary: '#89B4FA',
    background: '#181825',
    surface: '#1E1E2E',
    surfaceVariant: '#313244',
    onBackground: '#CDD6F4',
    onSurface: '#CDD6F4',
    outline: '#45475A',
  },
};

export default function App() {
  return (
    <SafeAreaProvider>
      <PaperProvider theme={theme}>
        <HomeScreen />
      </PaperProvider>
    </SafeAreaProvider>
  );
}
