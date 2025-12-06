import { useState, useEffect, useCallback } from 'react';

interface SpeechRecognitionEvent extends Event {
  results: SpeechRecognitionResultList;
  resultIndex: number;
}

interface SpeechRecognitionErrorEvent extends Event {
  error: string;
  message: string;
}

// Extend the global Window interface
declare global {
  interface Window {
    SpeechRecognition: any;
    webkitSpeechRecognition: any;
  }
}

interface UseVoiceSearchOptions {
  onResult: (transcript: string) => void;
  onError?: (error: string) => void;
  onListeningChange?: (isListening: boolean) => void;
  lang?: string;
}

const useVoiceSearch = ({
  onResult,
  onError,
  onListeningChange,
  lang = 'en-US' // Default to English, can be changed to hi-IN etc.
}: UseVoiceSearchOptions) => {
  const [isListening, setIsListening] = useState(false);
  const [recognition, setRecognition] = useState<any | null>(null);
  const [isSupported, setIsSupported] = useState(true);

  useEffect(() => {
    const SpeechRecognitionAPI = window.SpeechRecognition || window.webkitSpeechRecognition;
    if (!SpeechRecognitionAPI) {
      console.warn('Web Speech API is not supported in this browser.');
      setIsSupported(false);
      onError?.('Speech recognition not supported in this browser.');
      return;
    }

    const newRecognition = new SpeechRecognitionAPI();
    newRecognition.continuous = false; // Process single utterances
    newRecognition.interimResults = false; // We only want final results
    newRecognition.lang = lang;

    newRecognition.onstart = () => {
      setIsListening(true);
      onListeningChange?.(true);
    };

    newRecognition.onend = () => {
      setIsListening(false);
      onListeningChange?.(false);
    };

    newRecognition.onresult = (event: SpeechRecognitionEvent) => {
      const transcript = event.results[event.results.length - 1][0].transcript.trim();
      if (transcript) {
        onResult(transcript);
      }
    };

    newRecognition.onerror = (event: SpeechRecognitionErrorEvent) => {
      console.error('Speech recognition error:', event.error, event.message);
      setIsListening(false);
      onListeningChange?.(false);
      if (event.error === 'no-speech') {
        onError?.('No speech detected. Please try again.');
      } else if (event.error === 'audio-capture') {
        onError?.('Microphone problem. Please check your microphone.');
      } else if (event.error === 'not-allowed') {
        onError?.('Microphone access denied. Please allow microphone access.');
      } else {
        onError?.(`Error: ${event.error} - ${event.message}`);
      }
    };

    setRecognition(newRecognition);

    return () => {
      if (newRecognition) {
        newRecognition.abort();
      }
    };
  }, [lang, onResult, onError, onListeningChange]);

  const startListening = useCallback(async () => {
    if (!isSupported) {
      onError?.('Speech recognition not supported.');
      return;
    }
    if (recognition && !isListening) {
      try {
        // Check for microphone permission
        // Note: This is a simplified check. Robust permission handling might involve navigator.permissions.query
        await navigator.mediaDevices.getUserMedia({ audio: true }); // This will prompt for permission if not granted
        recognition.start();
      } catch (err: any) {
        console.error('Error starting speech recognition (mic permission likely):', err);
        if (err.name === 'NotAllowedError' || err.name === 'PermissionDeniedError') {
            onError?.('Microphone access denied. Please allow microphone access in your browser settings.');
        } else {
            onError?.('Could not start voice search. Please check microphone.');
        }
        setIsListening(false);
        onListeningChange?.(false);
      }
    }
  }, [recognition, isListening, isSupported, onError, onListeningChange]);

  const stopListening = useCallback(() => {
    if (recognition && isListening) {
      recognition.stop();
    }
  }, [recognition, isListening]);

  return {
    isListening,
    isSupported,
    startListening,
    stopListening,
  };
};

export default useVoiceSearch;
