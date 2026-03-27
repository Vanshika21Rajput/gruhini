// Global Custom Modal System for Gruhini
// Replaces browser confirm() dialogs with beautiful custom modals

window.CustomModal = {
    _currentCallback: null,

    confirm: function (message, onConfirm, onCancel) {
        const modal = document.createElement('div');
        modal.id = 'customConfirmModal';
        modal.className = 'fixed inset-0 bg-black/60 backdrop-blur-sm z-50 flex items-center justify-center p-4';
        modal.innerHTML = `
            <div class="bg-white rounded-2xl w-full max-w-sm p-6 shadow-2xl animate-fadeIn">
                <div class="flex items-start gap-3 mb-6">
                    <div class="w-10 h-10 rounded-full bg-amber-100 flex items-center justify-center flex-shrink-0">
                        <i data-lucide="alert-circle" class="w-5 h-5 text-amber-600"></i>
                    </div>
                    <div>
                        <h3 class="font-bold text-gray-900 mb-1">Confirm Action</h3>
                        <p class="text-sm text-gray-600">${message}</p>
                    </div>
                </div>
                
                <div class="flex gap-3">
                    <button id="modalCancel" 
                        class="flex-1 border-2 border-gray-300 text-gray-700 py-2.5 rounded-lg font-bold hover:bg-gray-50 transition">
                        Cancel
                    </button>
                    <button id="modalConfirm"
                        class="flex-1 bg-gradient-to-r from-amber-600 to-amber-700 text-white py-2.5 rounded-lg font-bold hover:shadow-lg transition">
                        Confirm
                    </button>
                </div>
            </div>
        `;

        document.body.appendChild(modal);

        // Re-init Lucide icons for the modal
        if (typeof lucide !== 'undefined') {
            lucide.createIcons();
        }

        // Store callbacks
        this._currentCallback = {
            onConfirm: onConfirm || (() => { }),
            onCancel: onCancel || (() => { })
        };

        // Add event listeners
        document.getElementById('modalConfirm').addEventListener('click', () => this.close(true));
        document.getElementById('modalCancel').addEventListener('click', () => this.close(false));

        // Close on background click
        modal.addEventListener('click', (e) => {
            if (e.target === modal) {
                this.close(false);
            }
        });
    },

    close: function (confirmed) {
        if (confirmed && this._currentCallback?.onConfirm) {
            this._currentCallback.onConfirm();
        } else if (!confirmed && this._currentCallback?.onCancel) {
            this._currentCallback.onCancel();
        }

        const modal = document.getElementById('customConfirmModal');
        if (modal) {
            modal.remove();
        }

        this._currentCallback = null;
    }
};
