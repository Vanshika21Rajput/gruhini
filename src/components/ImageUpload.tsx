import React, { useState } from 'react';
import { Button } from '@/components/ui/button';
import { Progress } from '@/components/ui/progress';
import { Upload, Camera, X, Loader2 } from 'lucide-react';
import { uploadToCloudinary, validateImageFile } from '@/utils/cloudinary';
import { toast } from 'sonner';

interface ImageUploadProps {
  currentImage?: string;
  onImageChange: (imageUrl: string) => void;
  disabled?: boolean;
}

const ImageUpload: React.FC<ImageUploadProps> = ({
  currentImage,
  onImageChange,
  disabled = false
}) => {
  const [isUploading, setIsUploading] = useState(false);
  const [uploadProgress, setUploadProgress] = useState(0);
  const [previewUrl, setPreviewUrl] = useState<string>(currentImage || '');

  const handleFileSelect = async (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (!file) return;

    // Validate file
    const validationError = validateImageFile(file);
    if (validationError) {
      toast.error(validationError);
      return;
    }

    // Create preview
    const localPreview = URL.createObjectURL(file);
    setPreviewUrl(localPreview);

    setIsUploading(true);
    setUploadProgress(0);

    try {
      // Simulate upload progress
      const progressInterval = setInterval(() => {
        setUploadProgress(prev => {
          if (prev >= 90) {
            clearInterval(progressInterval);
            return 90;
          }
          return prev + 10;
        });
      }, 200);

      // Upload to Cloudinary
      const cloudinaryUrl = await uploadToCloudinary(file);

      // Complete progress
      clearInterval(progressInterval);
      setUploadProgress(100);

      // Update with Cloudinary URL
      setPreviewUrl(cloudinaryUrl);
      onImageChange(cloudinaryUrl);

      toast.success('Image uploaded successfully!');

      // Clean up local preview URL
      URL.revokeObjectURL(localPreview);

    } catch (error) {
      console.error('Upload error:', error);
      toast.error('Failed to upload image. Please try again.');

      // Reset preview on error
      setPreviewUrl(currentImage || '');
      URL.revokeObjectURL(localPreview);
    } finally {
      setIsUploading(false);
      setUploadProgress(0);
    }

    // Clear the input
    event.target.value = '';
  };

  const handleRemoveImage = () => {
    if (previewUrl && previewUrl.startsWith('blob:')) {
      URL.revokeObjectURL(previewUrl);
    }
    setPreviewUrl('');
    onImageChange('');
    toast.success('Image removed');
  };

  return (
    <div className="space-y-4">
      <div className="flex items-center gap-4">
        <Button
          type="button"
          variant="outline"
          onClick={() => document.getElementById('image-upload')?.click()}
          disabled={disabled || isUploading}
        >
          {isUploading ? (
            <Loader2 className="h-4 w-4 mr-2 animate-spin" />
          ) : (
            <Upload className="h-4 w-4 mr-2" />
          )}
          {isUploading ? 'Uploading...' : 'Upload Image'}
        </Button>

        <Button
          type="button"
          variant="outline"
          disabled={disabled || isUploading}
          onClick={() => {
            // This would trigger camera on mobile devices
            const input = document.getElementById('camera-input') as HTMLInputElement;
            input?.click();
          }}
        >
          <Camera className="h-4 w-4 mr-2" />
          Take Photo
        </Button>

        <input
          id="image-upload"
          type="file"
          accept="image/*"
          onChange={handleFileSelect}
          className="hidden"
          disabled={disabled || isUploading}
        />

        <input
          id="camera-input"
          type="file"
          accept="image/*"
          capture="environment"
          onChange={handleFileSelect}
          className="hidden"
          disabled={disabled || isUploading}
        />
      </div>

      {isUploading && (
        <div className="space-y-2">
          <div className="flex items-center justify-between text-sm">
            <span>Uploading image...</span>
            <span>{uploadProgress}%</span>
          </div>
          <Progress value={uploadProgress} className="h-2" />
        </div>
      )}

      {previewUrl && (
        <div className="relative inline-block">
          <img
            src={previewUrl}
            alt="Product preview"
            className="w-32 h-32 object-cover rounded-lg border border-border"
          />
          {!isUploading && (
            <Button
              type="button"
              variant="outline"
              size="icon"
              className="absolute -top-2 -right-2 h-6 w-6 rounded-full bg-background border border-border"
              onClick={handleRemoveImage}
              disabled={disabled}
            >
              <X className="h-3 w-3" />
            </Button>
          )}
          {isUploading && (
            <div className="absolute inset-0 bg-black/50 rounded-lg flex items-center justify-center">
              <Loader2 className="h-6 w-6 text-white animate-spin" />
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default ImageUpload;
