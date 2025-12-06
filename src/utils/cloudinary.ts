// Cloudinary upload utility
export interface CloudinaryResponse {
  public_id: string;
  secure_url: string;
  width: number;
  height: number;
}

export const uploadToCloudinary = async (file: File): Promise<string> => {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('upload_preset', import.meta.env.VITE_CLOUDINARY_UPLOAD_PRESET);

  try {
    const response = await fetch(
      `https://api.cloudinary.com/v1_1/${import.meta.env.VITE_CLOUDINARY_CLOUD_NAME}/image/upload`,
      {
        method: 'POST',
        body: formData,
      }
    );

    if (!response.ok) {
      throw new Error(`Upload failed: ${response.statusText}`);
    }

    const data: CloudinaryResponse = await response.json();
    return data.secure_url;
  } catch (error) {
    console.error('Cloudinary upload error:', error);
    throw new Error('Failed to upload image to Cloudinary');
  }
};

export const validateImageFile = (file: File): string | null => {
  // Check file size (max 5MB)
  if (file.size > 5 * 1024 * 1024) {
    return 'Image size must be less than 5MB';
  }

  // Check file type
  const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/webp'];
  if (!allowedTypes.includes(file.type)) {
    return 'Only JPEG, PNG, and WebP images are allowed';
  }

  return null;
};